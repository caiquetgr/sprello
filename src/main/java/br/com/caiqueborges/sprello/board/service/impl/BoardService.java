package br.com.caiqueborges.sprello.board.service.impl;

import br.com.caiqueborges.sprello.board.repository.BoardRepository;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.CreateBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@Service
class BoardService implements CreateBoardService {

    private final BoardRepository boardRepository;

    @Override
    public Board createBoard(@Valid Board board) {
        return boardRepository.save(board);
    }

}
