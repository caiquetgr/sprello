package br.com.caiqueborges.sprello.board.service;

import br.com.caiqueborges.sprello.board.repository.entity.Board;

import javax.validation.Valid;

public interface UpdateBoardService {

    Board updateBoard(@Valid Board board);

}
