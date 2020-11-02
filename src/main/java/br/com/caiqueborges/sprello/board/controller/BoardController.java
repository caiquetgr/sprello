package br.com.caiqueborges.sprello.board.controller;

import br.com.caiqueborges.sprello.board.controller.mapper.BoardControllerMapper;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardRequest;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardResponse;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.CreateBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {

    private final CreateBoardService createBoardService;
    private final BoardControllerMapper boardControllerMapper;

    @PostMapping
    public ResponseEntity<CreateBoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {

        final Board board = createBoardService.createBoard(boardControllerMapper.createBoardRequestToBoard(request));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(boardControllerMapper.boardToCreateBoardResponse(board));

    }

}
