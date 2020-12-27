package br.com.caiqueborges.sprello.board.service;

import br.com.caiqueborges.sprello.board.repository.entity.Board;
import com.sun.istack.NotNull;

public interface ReadBoardService {

    Board getBoardById(@NotNull final Long boardId);

}
