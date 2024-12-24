package ru.practicum.enw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enw.model.entity.Comment;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {


    @Query("SELECT c FROM Comment c WHERE c.event.id = :idEvent " +
            "ORDER BY c.id LIMIT :size OFFSET :from")
    List<Comment> findByEventId(long idEvent, int size, int from);
}
