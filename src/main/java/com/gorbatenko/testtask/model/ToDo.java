package com.gorbatenko.testtask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "todo")
public class ToDo  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name="task")
    private String task;

    @Column(name="executed")
    private Boolean executed;

    public ToDo(User user, LocalDateTime dateTime, String task, boolean executed) {
        this.user = user;
        this.dateTime = dateTime;
        this.task = task;
        this.executed = executed;
    }
}
