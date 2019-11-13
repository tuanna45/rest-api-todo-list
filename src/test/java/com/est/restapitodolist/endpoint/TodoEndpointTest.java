package com.est.restapitodolist.endpoint;

import com.est.restapitodolist.model.Status;
import com.est.restapitodolist.model.Work;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TodoEndpointTest {
    private static final String WORK_URL = "/todo/works";

    @Resource
    private MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    @Test
    public void should_get_work_list_correctly() throws Exception {
        mockMvc.perform(get(WORK_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void should_get_bad_request_if_get_work_list_with_wrong_params() throws Exception {
        mockMvc.perform(get(WORK_URL)
                .param("sortOrder", "wrongValue"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_add_a_work_successfully() throws Exception {
        var work = Work.builder()
                .name("Testing work")
                .status(Status.PLANNING)
                .build();

        mockMvc.perform(post(WORK_URL)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(work)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(work.getName())));
    }

    @Test
    public void should_update_name_of_a_work_successfully() throws Exception {
        var work = Work.builder()
                .name("Testing work updated")
                .build();

        mockMvc.perform(patch(WORK_URL + "/{workId}", 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(work)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(work.getName())));
    }

    @Test
    public void should_get_not_found_if_update_a_wrong_work() throws Exception {
        var work = Work.builder()
                .name("Testing work updated")
                .build();

        mockMvc.perform(patch(WORK_URL + "/{workId}", 3L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(work)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_delete_a_work_successfully() throws Exception {
        mockMvc.perform(delete(WORK_URL + "/{workId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_get_not_found_if_delete_a_wrong_work() throws Exception {
        mockMvc.perform(delete(WORK_URL + "/{workId}", 3L))
                .andExpect(status().isNotFound());
    }
}
