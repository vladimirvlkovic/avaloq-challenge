package com.example.avaloq.challenge.integration;

import com.example.avaloq.challenge.DiceDistributionController;
import com.example.avaloq.challenge.DiceDistributionService;
import com.example.avaloq.challenge.model.DiceSimulation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DiceDistributionController.class)
public class DiceDistributionControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DiceDistributionService service;

    @Test
    public void givenDiceDistribution_whenGetDiceDistribution_returnLinkedHashMap() throws Exception {
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(1, 1);

        given(service.getDiceDistribution(1, 6, 1)).willReturn(distribution);

        mvc.perform(get("/api/dice-distribution")
                .param("diceNumber", "1")
                .param("sideNumber", "6")
                .param("rollNumber", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$.1", is(1)));

    }

    @Test
    public void givenDiceSimulations_whenGetDiceSimulations_returnJsonArray() throws Exception {
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(1, 1);

        DiceSimulation diceSimulation = new DiceSimulation();
        diceSimulation.setTotalNumberOfRolls(1);
        diceSimulation.setSimulationNumber(1);
        diceSimulation.setDiceNumber(1);
        diceSimulation.setSideNumber(6);

        DiceSimulation diceSimulation1 = new DiceSimulation();
        diceSimulation1.setTotalNumberOfRolls(1);
        diceSimulation1.setSimulationNumber(2);
        diceSimulation1.setDiceNumber(1);
        diceSimulation1.setSideNumber(7);

        List<DiceSimulation> diceSimulationList = new ArrayList<>();
        diceSimulationList.add(diceSimulation);
        diceSimulationList.add(diceSimulation1);

        given(service.getDiceSimulations()).willReturn(diceSimulationList);

        mvc.perform(get("/api/dice-simulation-list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].simulationNumber", is(1)))
                .andExpect(jsonPath("$[1].simulationNumber", is(2)));
    }

    @Test
    public void givenRelativeDiceDistribution_whenGetRelativeDiceDistribution_returnLinkedHashMap() throws Exception {
        Map<Integer, BigDecimal> distribution = new HashMap<>();
        distribution.put(1, new BigDecimal(100).setScale(2, RoundingMode.HALF_EVEN));

        given(service.getRelativeDistribution(1, 6)).willReturn(distribution);

        mvc.perform(get("/api/relative-dice-distribution")
                .param("diceNumber", "1")
                .param("sideNumber", "6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$.1", is(100.00)));

    }

    @Test
    public void givenNotValidParameters_whenGetDistribution_returnStatus400() throws Exception {
        mvc.perform(get("/api/dice-distribution")
                .param("diceNumber", "0")
                .param("sideNumber", "6")
                .param("rollNumber", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/dice-distribution")
                .param("diceNumber", "1")
                .param("sideNumber", "2")
                .param("rollNumber", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/dice-distribution")
                .param("diceNumber", "1")
                .param("sideNumber", "6")
                .param("rollNumber", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNotValidParameters_whenGetRelativeDistribution_returnStatus400() throws Exception {
        mvc.perform(get("/api/relative-dice-distribution")
                .param("diceNumber", "0")
                .param("sideNumber", "6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/relative-dice-distribution")
                .param("diceNumber", "1")
                .param("sideNumber", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}
