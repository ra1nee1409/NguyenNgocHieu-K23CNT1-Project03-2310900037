package k23cnt1.nnhlesson7.repository;
import k23cnt1.nnhlesson7.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends
        JpaRepository<Product, Long> {
}