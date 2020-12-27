package br.com.caiqueborges.sprello.board.controller.mapper;

import br.com.caiqueborges.sprello.board.controller.model.CreateBoardRequest;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardResponse;
import br.com.caiqueborges.sprello.board.controller.model.GetBoardByIdResponse;
import br.com.caiqueborges.sprello.board.repository.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardControllerMapper {

    Board createBoardRequestToBoard(CreateBoardRequest createBoardRequest);

    CreateBoardResponse boardToCreateBoardResponse(Board board);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "lastModifiedById", target = "lastModifiedBy")
    GetBoardByIdResponse boardToGetBoardByIdResponse(Board board);

}
