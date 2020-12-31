package br.com.caiqueborges.sprello.board.controller;

import br.com.caiqueborges.sprello.board.controller.mapper.BoardControllerMapper;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardRequest;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardResponse;
import br.com.caiqueborges.sprello.board.controller.model.GetBoardByIdResponse;
import br.com.caiqueborges.sprello.board.controller.model.UpdateBoardRequest;
import br.com.caiqueborges.sprello.board.controller.model.UpdateBoardResponse;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import br.com.caiqueborges.sprello.board.service.CreateBoardService;
import br.com.caiqueborges.sprello.board.service.DeleteBoardService;
import br.com.caiqueborges.sprello.board.service.ReadBoardService;
import br.com.caiqueborges.sprello.board.service.UpdateBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class BoardController {

    public static final String ENDPOINT_BOARDS = "/boards";
    public static final String ENDPOINT_BOARDS_BY_ID = ENDPOINT_BOARDS + "/{id}";

    private final CreateBoardService createBoardService;
    private final ReadBoardService readBoardService;
    private final DeleteBoardService deleteBoardService;
    private final UpdateBoardService updateBoardService;
    private final BoardControllerMapper boardControllerMapper;

    @PostMapping(ENDPOINT_BOARDS)
    public ResponseEntity<CreateBoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {

        final Board board = createBoardService.createBoard(boardControllerMapper.createBoardRequestToBoard(request));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(boardControllerMapper.boardToCreateBoardResponse(board));

    }

    @GetMapping(ENDPOINT_BOARDS_BY_ID)
    public ResponseEntity<GetBoardByIdResponse> getBoardById(@PathVariable("id") final Long id) {

        return Optional.of(id)
                .map(readBoardService::getBoardById)
                .map(boardControllerMapper::boardToGetBoardByIdResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping(ENDPOINT_BOARDS_BY_ID)
    public ResponseEntity<Void> deleteBoardById(@PathVariable("id") final Long id) {

        deleteBoardService.deleteBoardById(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping(ENDPOINT_BOARDS_BY_ID)
    public ResponseEntity<UpdateBoardResponse> updateBoardById(@PathVariable("id") final Long id,
                                                               @Valid @RequestBody UpdateBoardRequest request) {

        return Optional.of(boardControllerMapper.updateBoardRequestToBoard(id, request))
                .map(updateBoardService::updateBoard)
                .map(boardControllerMapper::boardToUpdateBoardResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());

    }

}
