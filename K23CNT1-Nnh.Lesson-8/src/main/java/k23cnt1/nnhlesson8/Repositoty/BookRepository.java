package k23cnt1.nnhlesson8.Repositoty;
import k23cnt1.nnhlesson8.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}