package br.com.caiqueborges.sprello.board.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardByIdResponse {

    private Long id;
    private String name;
    private Long createdBy;
    private ZonedDateTime createdDate;
    private Long lastModifiedBy;
    private ZonedDateTime lastModifiedDate;

}
