package ru.practicum.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.statsdto.HitObject;
import ru.practicum.statsdto.HitObjectDto;
import ru.practicum.stats.controller.StatsController;
import ru.practicum.stats.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
public class StatsControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    StatsService service;

    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    private final HitObject object = HitObject.builder()
            .id(1L)
            .app("some-app")
            .uri("/test/uri/1")
            .ip("127.0.0.1")
            .timestamp(LocalDateTime.now())
            .build();

    @Test
    void saveNewHit() throws Exception {

        when(service.hit(any())).thenReturn(object);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(object))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void getStats() throws Exception {

        HitObjectDto proj = new HitObjectDto("test-app", "test/uri", 4);

        when(service.viewStats(any())).thenReturn(List.of(proj));

        mvc.perform(get("/stats"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.*", hasSize(1))
                );


    }

}
