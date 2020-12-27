package br.com.caiqueborges.sprello.board.exception;

import br.com.caiqueborges.sprello.config.controlleradvice.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoardNotFoundException extends BaseException {

    private final Long boardId;

    public BoardNotFoundException(Long boardId) {
        this.boardId = boardId;
    }

    @Override
    public String getMessageKey() {
        return "board.notfound";
    }

    @Override
    public Map<String, Object> getMessageVariables() {
        return Map.of("id", boardId);
    }

}
