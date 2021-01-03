package br.com.caiqueborges.sprello.task.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatus {

    @Id
    private Long id;

    private String name;
    
    private String description;

}
