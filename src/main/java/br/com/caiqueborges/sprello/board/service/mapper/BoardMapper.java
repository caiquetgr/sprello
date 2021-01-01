package br.com.caiqueborges.sprello.board.service.mapper;

import br.com.caiqueborges.sprello.board.repository.entity.Board;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface BoardMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBoard(Board board, @MappingTarget Board boardPersisted);

}
