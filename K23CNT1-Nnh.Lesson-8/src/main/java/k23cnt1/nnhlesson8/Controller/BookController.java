package k23cnt1.nnhlesson8.Controller;

import k23cnt1.nnhlesson8.entity.Author;
import k23cnt1.nnhlesson8.entity.Book;
import k23cnt1.nnhlesson8.Service.AuthorService;
import k23cnt1.nnhlesson8.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    // Sửa đường dẫn giống AuthorController
    private static final String UPLOAD_DIR = "C:/Project 3/book_images/books/";

    // -------------------- Hiển thị danh sách sách --------------------
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/book-list";
    }

    // -------------------- Form thêm sách mới --------------------
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "books/book-form";
    }

    // -------------------- Lưu sách (thêm mới hoặc cập nhật) --------------------
    @PostMapping("/save")
    public String saveBook(
            @ModelAttribute Book book,
            @RequestParam(required = false) List<Integer> authorIds,
            @RequestParam("imageBook") MultipartFile imageFile) {

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if (!imageFile.isEmpty()) {
                String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                // Sử dụng code + timestamp để tránh trùng tên file (giống AuthorController)
                String newFileName = book.getCode().replaceAll("\\s+", "_")
                        + "_" + System.currentTimeMillis() + fileExtension;

                Path filePath = uploadPath.resolve(newFileName);

                // Nếu file đã tồn tại, xóa để tránh FileAlreadyExistsException
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }

                Files.copy(imageFile.getInputStream(), filePath);

                // SỬA QUAN TRỌNG: Lưu URL để hiển thị - giống cấu trúc AuthorController
                book.setImgUrl("/uploads/books/" + newFileName);
            } else if (book.getId() != null) {
                // Edit mà không upload ảnh mới -> giữ ảnh cũ
                Book existingBook = bookService.getBookById(book.getId());
                book.setImgUrl(existingBook.getImgUrl());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gán authors
        if (authorIds != null && !authorIds.isEmpty()) {
            List<Author> authors = authorService.findAllById(authorIds);
            book.setAuthors(authors);
        }

        bookService.saveBook(book);
        return "redirect:/books";
    }

    // -------------------- Form sửa sách --------------------
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAllAuthors());
        return "books/book-form";
    }

    // -------------------- Xóa sách --------------------
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Integer id) {
        // Xóa file ảnh trước khi xóa database (giống AuthorController)
        Book book = bookService.getBookById(id);
        if (book.getImgUrl() != null) {
            String fileName = book.getImgUrl().substring(book.getImgUrl().lastIndexOf("/") + 1);
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bookService.deleteBook(id);
        return "redirect:/books";
    }
}