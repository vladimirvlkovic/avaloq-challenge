package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.DiceSimulation;
import com.example.avaloq.challenge.model.DiceSimulationPrimaryKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DiceDistributionService {

    Random random = new Random();
    final DiceSimulationRepository diceSimulationRepository;

    public Map<Integer,Integer> getDiceDistribution(int numberOfDice, int numberOfDiceSides, int numberOfRolls) {
        int totalSumOfRoll;
        HashMap<Integer,Integer> diceDistribution = new HashMap<>();
        for(int i = 0; i < numberOfRolls; i++) {
           totalSumOfRoll = getRollSum(numberOfDice, numberOfDiceSides);
            if(diceDistribution.containsKey(totalSumOfRoll)) {
                diceDistribution.replace(totalSumOfRoll, diceDistribution.get(totalSumOfRoll) + 1);
            } else {
                diceDistribution.put(totalSumOfRoll, 1);
            }
        }
        DiceSimulationPrimaryKey diceSimulationPrimaryKey = getDiceSimulationPrimaryKey(numberOfDice, numberOfDiceSides);
        saveDiceDistribution(diceSimulationPrimaryKey,numberOfRolls,diceDistribution);
        return diceDistribution;
    }

    private int getRollSum(int numberOfDice, int numberOfDiceSides) {
        int totalSumOfRoll = 0;
        for(int i = 0; i < numberOfDice; i++){
            totalSumOfRoll += random.ints(1, numberOfDiceSides + 1).findFirst().getAsInt();
        }
        return totalSumOfRoll;
    }

    private DiceSimulationPrimaryKey getDiceSimulationPrimaryKey(int numberOfDice, int numberOfDiceSides) {
        return new DiceSimulationPrimaryKey(numberOfDice, numberOfDiceSides);
    }

    private void saveDiceDistribution(DiceSimulationPrimaryKey diceSimulationPrimaryKey, int numberOfRolls, Map<Integer, Integer> diceDistribution) {
        DiceSimulation diceSimulation;

        if (diceSimulationRepository.findById(diceSimulationPrimaryKey).isPresent()) {
            diceSimulation = diceSimulationRepository.findById(diceSimulationPrimaryKey).get();
            diceSimulation = updateDiceSimulation(diceSimulation, numberOfRolls, diceDistribution);
        } else {
            diceSimulation = initDiceSimulation(diceSimulationPrimaryKey, numberOfRolls, diceDistribution);
        }
        diceSimulationRepository.save(diceSimulation);
        System.out.println(diceSimulationRepository.findById(diceSimulationPrimaryKey));
    }

    private DiceSimulation updateDiceSimulation(DiceSimulation diceSimulation, int numberOfRolls, Map<Integer, Integer> diceDistribution) {
        DiceSimulation updatedDiceSimulation = diceSimulation;
        updatedDiceSimulation.setNumberOfSimulations(diceSimulation.getNumberOfSimulations() + 1);
        updatedDiceSimulation.setTotalRollsMade(diceSimulation.getTotalRollsMade() + numberOfRolls);
        Map<Integer,Integer> databaseDiceDistribution = diceSimulation.getDiceTotalSumCount();
        diceDistribution.forEach((key, value) -> databaseDiceDistribution.merge(key, value, Integer::sum));
        return updatedDiceSimulation;
    }

    private DiceSimulation initDiceSimulation(DiceSimulationPrimaryKey diceSimulationPrimaryKey, int numberOfRolls, Map<Integer, Integer> diceDistribution) {
        DiceSimulation diceSimulation = new DiceSimulation();
        diceSimulation.setDiceSimulationPrimaryKey(diceSimulationPrimaryKey);
        diceSimulation.setNumberOfSimulations(1);
        diceSimulation.setTotalRollsMade(numberOfRolls);
        diceSimulation.setDiceTotalSumCount(diceDistribution);
        return  diceSimulation;
    }

}
