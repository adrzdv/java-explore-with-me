package ru.practicum.enw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.enw.model.entity.User;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User AS u " +
            "WHERE id IN :ids ORDER BY u.id")
    List<User> getUsersWithIds(List<Integer> ids);

    @Query("SELECT u FROM User AS u " +
            "ORDER BY u.id LIMIT :size OFFSET :from")
    List<User> getUsersWithLimit(int from, int size);

    @Query("SELECT u FROM User AS u " +
            "WHERE id IN :ids ORDER BY u.id LIMIT :size OFFSET :from")
    List<User> getUsersWithIdsAndLimit(List<Integer> ids, int from, int size);

}
