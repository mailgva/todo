package com.gorbatenko.testtask.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoTo {
    private Long id;
    private LocalDateTime dateTime;
    private String task;
    private Boolean executed;
}
