package br.com.caiqueborges.sprello.board.service;

import javax.validation.constraints.NotNull;

public interface DeleteBoardService {

    void deleteBoardById(@NotNull final Long boardId);

}
