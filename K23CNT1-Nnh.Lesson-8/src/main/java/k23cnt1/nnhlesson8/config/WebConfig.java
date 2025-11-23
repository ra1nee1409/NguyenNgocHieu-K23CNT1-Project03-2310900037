package k23cnt1.nnhlesson8.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Thư mục gốc chứa toàn bộ ảnh
        String rootFolder = "C:/Project 3/book_images/";

        // Ảnh Sách
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:" + rootFolder + "books/");

        // Ảnh Tác Giả
        registry.addResourceHandler("/uploads/authors/**")
                .addResourceLocations("file:" + rootFolder + "authors/");
    }
}
