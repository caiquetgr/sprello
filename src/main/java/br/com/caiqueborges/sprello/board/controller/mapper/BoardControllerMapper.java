package br.com.caiqueborges.sprello.board.controller.mapper;

import br.com.caiqueborges.sprello.board.controller.model.CreateBoardRequest;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardResponse;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardControllerMapper {

    Board createBoardRequestToBoard(CreateBoardRequest createBoardRequest);

    CreateBoardResponse boardToCreateBoardResponse(Board board);

}
