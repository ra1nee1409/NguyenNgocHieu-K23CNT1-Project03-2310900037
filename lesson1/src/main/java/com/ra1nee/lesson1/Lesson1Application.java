package com.ra1nee.lesson1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@FunctionalInterface
interface Hello1 {
    void sayHello(); // phương thức trừu tượng
}

// Lớp chính để chạy chương trình
public class Lesson1Application {
    public static void main(String[] args) {

        // Sử dụng Lambda Expression để cài đặt phương thức sayHello()
        Hello1 sayHello = () -> {
            System.out.println("Hello World");
        };

        // Gọi phương thức sayHello()
        sayHello.sayHello();
    }
}
