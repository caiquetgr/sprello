package br.com.caiqueborges.sprello.board.service;

import br.com.caiqueborges.sprello.board.repository.entity.Board;

import javax.validation.Valid;

public interface CreateBoardService {

    Board createBoard(@Valid Board board);

}
