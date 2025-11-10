package com.nnh.lesson3.controller;
import com.nnh.lesson3.entity.Student;
import com.nnh.lesson3.service.ServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class StudentController {
    @Autowired
    private ServiceStudent studentService;
    @GetMapping("/student-list")
    public List<Student> getAllStudents() {
        return studentService.getStudents();
    }
    @GetMapping("/student/{id}")
    public Student getAllStudents(@PathVariable String id)
    {
        Long param = Long.parseLong(id);
        return studentService.getStudent(param);
    }
    @PostMapping("/student-add")
    public Student addStudent(@RequestBody Student student)
    {
        return studentService.addStudent(student);
    }
    @PutMapping("/student/{id}")
    public Student updateStudent(@PathVariable String id,
                                 @RequestBody Student student) {
        Long param = Long.parseLong(id);
        return studentService.updateStudent(param,
                student);
    }
    @DeleteMapping("/student/{id}")
    public boolean deleteStudent(@PathVariable String id) {
        Long param = Long.parseLong(id);
        return studentService.deleteStudent(param);
    }
}