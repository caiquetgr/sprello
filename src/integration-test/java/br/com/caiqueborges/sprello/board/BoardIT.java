package br.com.caiqueborges.sprello.board;

import br.com.caiqueborges.sprello.AbstractIT;
import br.com.caiqueborges.sprello.board.controller.model.CreateBoardResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardIT extends AbstractIT {

    @SneakyThrows
    @Test
    void whenValidCreateBoard_thenReturn201AndCreatedEntity() {

        MockHttpServletResponse response = this.mockMvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        CreateBoardResponse createBoardResponse = objectMapper
                .readValue(response.getContentAsString(), CreateBoardResponse.class);

        assertThat(createBoardResponse.getId()).isEqualTo(1L);
        assertThat(createBoardResponse.getName()).isEqualTo("Test");

    }

    @SneakyThrows
    @Test
    void whenInvalidCreateBoardWithNameEmpty_thenReturn400() {

        this.mockMvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"The field \\\"name\\\" can't be empty\"")));

    }

    @SneakyThrows
    @Test
    void whenInvalidCreateBoardWithContentNull_thenReturn400() {

        this.mockMvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}
