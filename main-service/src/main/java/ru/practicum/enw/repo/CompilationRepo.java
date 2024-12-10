package ru.practicum.enw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enw.model.entity.Compilation;

import java.util.List;

public interface CompilationRepo extends JpaRepository<Compilation, Long> {

    @Query("SELECT c FROM Compilation AS c WHERE " +
            ":pinned = false OR c.pinned = :pinned " +
            "ORDER BY c.id LIMIT :size OFFSET :from")
    List<Compilation> getCompilationsByParams(Boolean pinned, int size, int from);
}
