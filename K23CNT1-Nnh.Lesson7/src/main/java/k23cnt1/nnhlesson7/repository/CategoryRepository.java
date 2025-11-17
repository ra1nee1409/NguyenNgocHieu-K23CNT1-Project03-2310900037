package k23cnt1.nnhlesson7.repository;
import k23cnt1.nnhlesson7.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepository extends
        JpaRepository<Category, Long> {
}