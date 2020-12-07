package br.com.caiqueborges.sprello.board;

import br.com.caiqueborges.sprello.AuthenticatedIT;
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

public class BoardIT extends AuthenticatedIT {

    private static final String BOARDS_RESOURCE = "/boards";

    @SneakyThrows
    @Test
    void whenCreateBoard_thenReturn201AndCreatedEntity() {

        MockHttpServletResponse response = this.performAuthenticated(post(BOARDS_RESOURCE)
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
    void whenCreateBoard_withoutAuthentication_thenReturn401() {

        this.mockMvc.perform(post(BOARDS_RESOURCE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test\"}"))
                .andExpect(status().isUnauthorized());

    }

    @SneakyThrows
    @Test
    void whenCreateBoard_withNameEmpty_thenReturn400() {

        performAuthenticated(post(BOARDS_RESOURCE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"The field \\\"name\\\" can't be empty\"")));

    }

    @SneakyThrows
    @Test
    void whenCreateBoard_withContentNull_thenReturn400() {

        performAuthenticated(post(BOARDS_RESOURCE).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}
