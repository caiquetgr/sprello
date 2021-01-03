package br.com.caiqueborges.sprello.task.repository.entity;

import br.com.caiqueborges.sprello.base.repository.entity.UserAuditedEntity;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"board", "taskStatus"})
@EqualsAndHashCode(exclude = {"board", "taskStatus"})
public class Task extends UserAuditedEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board", insertable = false, updatable = false)
    private Long boardId;

    @JoinColumn(name = "board", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Board board;

    @NotBlank(message = "{task.name.notempty}", groups = Default.class)
    @Size(max = 100, message = "{task.name.size.max}", groups = Default.class)
    private String name;

    @NotBlank(message = "{task.description.notempty}", groups = Default.class)
    @Size(max = 500, message = "{task.description.size.max}", groups = Default.class)
    private String description;

    @JoinColumn(name = "task_status", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TaskStatus taskStatus;

}
