package com.ra1nee.lesson1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@FunctionalInterface
interface Hello1 {
    void sayHello(); // phương thức trừu tượng
}
@SpringBootTest
class Lesson1ApplicationTests {

    public static void main(String[] args) {

        // Sử dụng Lambda Expression để cài đặt phương thức sayHello()
        Hello1 sayHello = () -> {
            System.out.println("Hello World");
        };

        // Gọi phương thức sayHello()
        sayHello.sayHello();
    }
}
