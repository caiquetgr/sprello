package br.com.caiqueborges.sprello.board.service.impl;

import br.com.caiqueborges.sprello.base.web.service.JwtRequestService;
import br.com.caiqueborges.sprello.board.exception.BoardNotFoundException;
import br.com.caiqueborges.sprello.board.repository.BoardRepository;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.CreateBoardService;
import br.com.caiqueborges.sprello.board.service.DeleteBoardService;
import br.com.caiqueborges.sprello.board.service.ReadBoardService;
import br.com.caiqueborges.sprello.board.service.UpdateBoardService;
import br.com.caiqueborges.sprello.board.service.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RequiredArgsConstructor
@Service
class BoardService implements CreateBoardService, ReadBoardService, DeleteBoardService, UpdateBoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final JwtRequestService jwtRequestService;

    @Transactional
    @Override
    public Board createBoard(@Valid Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board getBoardById(final Long boardId) {
        return findBoardByIdAndUserCreated(boardId);
    }

    private Board findBoardByIdAndUserCreated(Long boardId) throws BoardNotFoundException {

        final Long requestUserId = jwtRequestService.getRequestUserId();

        return boardRepository.findByIdAndCreatedByIdAndDeletedFalse(boardId, requestUserId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

    }

    @Transactional
    @Override
    public void deleteBoardById(@NotNull Long boardId) {

        final Board board = findBoardByIdAndUserCreated(boardId);

        boardRepository.deleteLogicallyById(board);

    }

    @Transactional
    @Override
    public Board updateBoard(@Valid Board board) {

        final Board boardPersisted = findBoardByIdAndUserCreated(board.getId());

        boardMapper.updateBoard(board, boardPersisted);

        return boardRepository.save(boardPersisted);

    }

}
