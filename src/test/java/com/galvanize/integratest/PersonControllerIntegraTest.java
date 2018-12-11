package com.galvanize.integratest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIntegraTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(PersonControllerIntegraTest.class);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository repository;

    @MockBean
    RestTemplate restTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        repository.deleteAll();
        Person savedPerson;
        for (int i = 1; i <= 5; i++) {
            savedPerson = repository.save(Person.builder()
                    .firstName("firstName" + i)
                    .lastName("lastName" + i)
                    .build());
            LOGGER.info("add Person record: {}", savedPerson);
        }
    }

    @Test
    public void getAll() throws Exception {
        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok("mock@email.com"));

        String response = mockMvc.perform(MockMvcRequestBuilders
                .get("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Person> people = mapper.readValue(response, new TypeReference<List<Person>>(){});

        verify(restTemplate, times(5)).getForEntity(anyString(), any());

        assertThat(people.size(), is(5));
    }
}