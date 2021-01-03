package br.com.caiqueborges.sprello.task.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "{task.name.notempty}")
    @Size(max = 100, message = "{task.name.size.max}")
    private String name;

    @NotBlank(message = "{task.description.notempty}")
    @Size(max = 500, message = "{task.description.size.max}")
    private String description;

}
