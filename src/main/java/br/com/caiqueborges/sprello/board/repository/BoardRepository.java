package br.com.caiqueborges.sprello.board.repository;

import br.com.caiqueborges.sprello.base.repository.UserAuditedRepository;
import br.com.caiqueborges.sprello.board.repository.entity.Board;

import java.util.Optional;

public interface BoardRepository extends UserAuditedRepository<Board, Long> {

    Optional<Board> findByIdAndCreatedByIdAndDeletedFalse(Long boardId, Long createdById);

}
