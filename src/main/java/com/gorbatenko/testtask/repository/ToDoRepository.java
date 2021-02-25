package com.gorbatenko.testtask.repository;

import com.gorbatenko.testtask.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    @Query(value = "SELECT * FROM todo t WHERE t.user_id = ?1 ORDER BY t.date_time ASC", nativeQuery = true)
    List<ToDo> findAllByUserId(long user_id);

    @Query(value = "SELECT * FROM todo t WHERE t.id = ?1 and t.user_id = ?2", nativeQuery = true)
    Optional<ToDo> findByIdAndUserId(long id, long user_id);

}
