package br.com.caiqueborges.sprello.board.service;

import br.com.caiqueborges.sprello.board.repository.entity.Board;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

public interface CreateBoardService {

    @Validated(Board.ValidBoardSave.class)
    Board createBoard(@Valid Board board);

}
