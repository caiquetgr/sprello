package br.com.caiqueborges.sprello.board.repository.entity;

import br.com.caiqueborges.sprello.base.repository.entity.UserAuditedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Table(name = "boards")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board extends UserAuditedEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{board.name.notempty}", groups = ValidBoardSave.class)
    @Size(max = 40, message = "{board.name.size.max}", groups = ValidBoardSave.class)
    @Column(name = "name", nullable = false)
    private String name;

    public interface ValidBoardSave {
    }

}
