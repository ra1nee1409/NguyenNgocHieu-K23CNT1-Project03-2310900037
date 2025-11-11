package K23CNT1_Nnh.Lesson6.repository;

import K23CNT1_Nnh.Lesson6.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
