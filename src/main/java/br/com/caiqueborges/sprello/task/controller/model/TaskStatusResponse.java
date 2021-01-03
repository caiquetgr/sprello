package br.com.caiqueborges.sprello.task.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusResponse {

    private Long id;
    private String name;
    private String description;

}
