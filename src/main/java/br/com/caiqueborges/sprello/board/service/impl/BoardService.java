package br.com.caiqueborges.sprello.board.service.impl;

import br.com.caiqueborges.sprello.board.exception.BoardNotFoundException;
import br.com.caiqueborges.sprello.board.repository.BoardRepository;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.CreateBoardService;
import br.com.caiqueborges.sprello.board.service.DeleteBoardService;
import br.com.caiqueborges.sprello.board.service.ReadBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RequiredArgsConstructor
@Service
class BoardService implements CreateBoardService, ReadBoardService, DeleteBoardService {

    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public Board createBoard(@Valid Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board getBoardById(final Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
    }

    @Transactional
    @Override
    public void deleteBoardById(@NotNull Long boardId) {
        boardRepository.deleteLogicallyById(boardId);
    }

}
