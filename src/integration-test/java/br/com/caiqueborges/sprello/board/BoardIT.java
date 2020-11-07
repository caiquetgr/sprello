package br.com.caiqueborges.sprello.board;

import br.com.caiqueborges.sprello.board.controller.model.CreateBoardResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
