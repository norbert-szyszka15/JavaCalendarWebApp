// src/test/java/org/example/javacalendarwebapp/controller/PriorityControllerTest.java
package org.example.javacalendarwebapp.controller;

import org.example.javacalendarwebapp.priority.HighPriority;
import org.example.javacalendarwebapp.priority.LowPriority;
import org.example.javacalendarwebapp.priority.MediumPriority;
import org.example.javacalendarwebapp.priority.PriorityLevel;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PriorityControllerTest {

    @Test
    void getPriorities_ReturnsAllLevels() throws Exception {
        PriorityLevel high = new HighPriority();
        PriorityLevel medium = new MediumPriority();
        PriorityLevel low = new LowPriority();
        PriorityController controller = new PriorityController(List.of(high, medium, low));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/prorities"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                   ["High Priority","Medium Priority","Low Priority"]
                   """));
    }
}
