package com.example.ssak3.domain.category.repository;

import com.example.ssak3.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndIsDeletedFalse(Long id);

    List<Category> findByIsDeletedFalse();

    boolean existsByNameAndIsDeletedFalse(String name);

    boolean existsByNameAndIsDeletedFalseAndIdNot(String name, Long id);

    Object name(String name);
}
