package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.Dice;
import com.example.avaloq.challenge.model.DiceDistribution;
import com.example.avaloq.challenge.model.DiceSimulation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DiceDistributionService {

    Random random = new Random();
    final DiceDistributionRepository diceDistributionRepository;

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
        saveDiceDistribution(numberOfDice, numberOfDiceSides,numberOfRolls,diceDistribution);
        return diceDistribution;
    }


    private int getRollSum(int numberOfDice, int numberOfDiceSides) {
        int totalSumOfRoll = 0;
        for(int i = 0; i < numberOfDice; i++){
            totalSumOfRoll += random.ints(1, numberOfDiceSides + 1).findFirst().getAsInt();
        }
        return totalSumOfRoll;
    }

    private void saveDiceDistribution(int numberOfDice, int numberOfDiceSides, int numberOfRolls, Map<Integer, Integer> diceDistribution) {
        DiceDistribution diceDistributionDbObject = new DiceDistribution();
        diceDistributionDbObject.setNumberOfDice(numberOfDice);
        diceDistributionDbObject.setNumberOfDiceSides(numberOfDiceSides);
        diceDistributionDbObject.setNumberOfRolls(numberOfRolls);
        diceDistributionDbObject.setDiceDistribution(diceDistribution);
        diceDistributionRepository.save(diceDistributionDbObject);
    }

    public List<DiceSimulation> getDiceSimulations() {
        List<Dice.NumberOfDiceAndSides> distinctNumberOfDiceAndSidesList;
        distinctNumberOfDiceAndSidesList = diceDistributionRepository.findDistinctBy();
        List<DiceSimulation> diceSimulationList = new ArrayList<>();
        for(int i = 0; i < distinctNumberOfDiceAndSidesList.size(); i++) {
            Dice.NumberOfDiceAndSides tmp = distinctNumberOfDiceAndSidesList.get(i);
            DiceSimulation diceSimulation = getDiceSimulation(tmp);
            diceSimulationList.add(diceSimulation);
        }
        return diceSimulationList;
    }

    private DiceSimulation getDiceSimulation(Dice.NumberOfDiceAndSides numberOfDiceAndSides) {
        int numberOfDice = numberOfDiceAndSides.getNumberOfDice();
        int numberOfDiceSides = numberOfDiceAndSides.getNumberOfDiceSides();
        DiceSimulation diceSimulation = new DiceSimulation();
        diceSimulation.setNumberOfDiceSides(numberOfDiceSides);
        diceSimulation.setSimulationNumber(diceDistributionRepository.countByNumberOfDiceAndNumberOfDiceSides(numberOfDice, numberOfDiceSides));
        diceSimulation.setNumberOfDice(numberOfDice);
        diceSimulation.setTotalNumberOfRolls(diceDistributionRepository.sumTotalRollsByNumberOfDiceAndNumberOfDiceSides(numberOfDice, numberOfDiceSides));
        return diceSimulation;
    }

}
