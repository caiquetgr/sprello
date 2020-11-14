package br.com.caiqueborges.sprello.board.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBoardRequest {

    @NotBlank(message = "{board.name.notempty}")
    @Size(max = 40, message = "{board.name.size.max}")
    private String name;

}
