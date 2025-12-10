package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NnhCategoryRepository extends JpaRepository<NnhCategory, Long> {

}
