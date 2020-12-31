package br.com.caiqueborges.sprello.board.service;

import br.com.caiqueborges.sprello.board.repository.entity.Board;
import com.sun.istack.NotNull;
import org.springframework.validation.annotation.Validated;

public interface ReadBoardService {

    @Validated
    Board getBoardById(@NotNull final Long boardId);

}
