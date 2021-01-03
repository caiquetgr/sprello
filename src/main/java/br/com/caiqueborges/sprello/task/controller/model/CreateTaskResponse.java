package br.com.caiqueborges.sprello.task.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskResponse {

    private Long id;
    private Long boardId;
    private String name;
    private String description;
    private TaskStatusResponse taskStatus;

}
