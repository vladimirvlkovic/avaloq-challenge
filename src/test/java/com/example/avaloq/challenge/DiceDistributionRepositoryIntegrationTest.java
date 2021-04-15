package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.Dice;
import com.example.avaloq.challenge.model.DiceDistribution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DiceDistributionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DiceDistributionRepository repository;


    DiceDistribution diceDistribution;
    DiceDistribution diceDistribution1;

    @Before
    public void init() {
        diceDistribution = new DiceDistribution();
        Map<Integer, Integer> dist = new HashMap<>();
        dist.put(2, 1);
        diceDistribution.setDiceNumber(1);
        diceDistribution.setSideNumber(6);
        diceDistribution.setRollNumber(1);
        diceDistribution.setDiceDistribution(dist);

        diceDistribution1 = new DiceDistribution();
        diceDistribution1.setDiceNumber(1);
        diceDistribution1.setSideNumber(6);
        diceDistribution1.setRollNumber(1);
        diceDistribution1.setDiceDistribution(dist);

        entityManager.persist(diceDistribution);
        entityManager.persist(diceDistribution1);
        entityManager.flush();


    }

    @Test
    public void whenFindByDiceNumberAndSideNumber_thenReturnDiceDistributionList() {

        List<DiceDistribution> expectedDistributionList = new ArrayList<>();
        expectedDistributionList.add(diceDistribution);
        expectedDistributionList.add(diceDistribution1);

        List<DiceDistribution> foundDistributions = repository.findByDiceNumberAndSideNumber(diceDistribution.getDiceNumber(), diceDistribution.getSideNumber());
        assertEquals(expectedDistributionList, foundDistributions);
    }

    @Test
    public void whenCountByDiceNumberAndSideNumber_thenReturnCount() {
        long foundCount = repository.countByDiceNumberAndSideNumber(1, 6);
        assertEquals(2, foundCount);
    }

    @Test
    public void whenSumTotalRollsByDiceNumberAndSideNumber_thenReturnSumOfRolls() {
        long foundSumOfRolls = repository.sumTotalRollsByDiceNumberAndSideNumber(1, 6);
        assertEquals(2, foundSumOfRolls);
    }

    @Test
    public void whenFindDistinctBy_thenReturnDistinctListOfDiceAndSidesNumber() {
        List<Dice.DiceAndSidesNumber> foundDiceAndSidesNumberList = repository.findDistinctBy();

        assertEquals(1, foundDiceAndSidesNumberList.size());
        assertEquals(1, foundDiceAndSidesNumberList.get(0).getDiceNumber());
        assertEquals(6, foundDiceAndSidesNumberList.get(0).getSideNumber());
    }
}
