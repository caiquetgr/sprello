package br.com.caiqueborges.sprello.task.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTaskByIdResponse {

    private Long id;
    private Long boardId;
    private String name;
    private String description;
    private TaskStatusResponse taskStatus;
    private Long createdBy;
    private ZonedDateTime createdDate;
    private Long lastModifiedBy;
    private ZonedDateTime lastModifiedDate;
    
}
