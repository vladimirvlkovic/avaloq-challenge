package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.Dice;
import com.example.avaloq.challenge.model.DiceDistribution;
import com.example.avaloq.challenge.model.DiceSimulation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.criteria.CriteriaBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ChallengeApplicationTests {

	@Mock
	DiceDistributionRepository repository;
	@InjectMocks
	DiceDistributionService service;
	@Mock
	Dice.DiceAndSidesNumber diceAndSidesNumber;

	Map<Integer, Integer> result;

	int diceNumber = 2;
	int sideNumber = 6;
	int rollNumber = 10000;

	@Before
	public void init() {
		result = service.getDiceDistribution(diceNumber, sideNumber, rollNumber);
	}

	@Test
	public void getDiceDistribution_RollNumber_Equal() {
		int sumOfRolls = result.values().stream().reduce(0, Integer::sum);
		assertEquals(rollNumber, sumOfRolls);
	}

	@Test
	public void getDiceDistribution_MinDiceSumIsLessThenNumberOfDice_False() {
		assertFalse(result.containsKey(diceNumber - 1));
	}

	@Test
	public void getDiceDistribution_MaxDiceSumIsMoreThenMaxDiceSum_False() {
		assertFalse(result.containsKey((diceNumber * sideNumber) + 1));
	}

	@Test
	public void getSimulations_SimulationList_Equal() {
		when(diceAndSidesNumber.getDiceNumber()).thenReturn(diceNumber);
		when(diceAndSidesNumber.getSideNumber()).thenReturn(sideNumber);
		List<Dice.DiceAndSidesNumber> list = new ArrayList<>();
		list.add(diceAndSidesNumber);
		when(repository.findDistinctBy()).thenReturn(list);
		when(repository.countByDiceNumberAndSideNumber(diceNumber,sideNumber)).thenReturn(1L);
		when(repository.sumTotalRollsByDiceNumberAndSideNumber(diceNumber,sideNumber)).thenReturn((long) rollNumber);
		List<DiceSimulation> expectedDiceSimulationList = new ArrayList<>();
		DiceSimulation expectedDiceSimulation = new DiceSimulation();
		expectedDiceSimulation.setSimulationNumber(1L);
		expectedDiceSimulation.setDiceNumber(diceNumber);
		expectedDiceSimulation.setSideNumber(sideNumber);
		expectedDiceSimulation.setTotalNumberOfRolls(rollNumber);
		expectedDiceSimulationList.add(expectedDiceSimulation);
		assertEquals(expectedDiceSimulationList, service.getDiceSimulations());
	}

	@Test
	public void getRelativeDistribution_RelativeDistribution_Equal() {
		Map<Integer, Integer> distribution = new HashMap<>();
		distribution.put(2, 1000);
		distribution.put(3, 2000);
		distribution.put(4, 3000);
		List<DiceDistribution> diceDistributionList = new ArrayList<>();
		DiceDistribution diceDistribution = new DiceDistribution();
		diceDistribution.setDiceNumber(diceNumber);
		diceDistribution.setSideNumber(sideNumber);
		diceDistribution.setRollNumber(rollNumber);
		diceDistribution.setDiceDistribution(distribution);
		diceDistributionList.add(diceDistribution);
		when(repository.findByDiceNumberAndSideNumber(diceNumber, sideNumber)).thenReturn(diceDistributionList);
		when(repository.sumTotalRollsByDiceNumberAndSideNumber(diceNumber,sideNumber)).thenReturn((long) rollNumber);
		Map<Integer, BigDecimal> expectedRelativeDistribution = new HashMap<>();
		expectedRelativeDistribution.put(2, new BigDecimal(1000.0 / (rollNumber / 100.0)).setScale(2, RoundingMode.HALF_EVEN));
		expectedRelativeDistribution.put(3, new BigDecimal(2000.0 / (rollNumber / 100.0)).setScale(2, RoundingMode.HALF_EVEN));
		expectedRelativeDistribution.put(4, new BigDecimal(3000.0 / (rollNumber / 100.0)).setScale(2, RoundingMode.HALF_EVEN));
		assertEquals(expectedRelativeDistribution, service.getRelativeDistribution(diceNumber, sideNumber));
	}

}
