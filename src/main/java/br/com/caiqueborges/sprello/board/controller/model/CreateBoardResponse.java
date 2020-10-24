package br.com.caiqueborges.sprello.board.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBoardResponse {
    private Long id;
    private String name;
}
