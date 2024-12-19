package ru.practicum.enw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.enw.model.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category AS c " +
            "ORDER BY c.id LIMIT :size OFFSET :from")
    List<Category> getCategoriesByParams(int from, int size);
}
