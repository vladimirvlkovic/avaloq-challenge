package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.Dice;
import com.example.avaloq.challenge.model.DiceDistribution;
import com.example.avaloq.challenge.model.DiceSimulation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiceDistributionService {

    Random random = new Random();
    final DiceDistributionRepository repository;

    public Map<Integer,Integer> getDiceDistribution(int diceNumber, int sideNumber, int rollNumber) {
        int totalSumOfRoll;
        HashMap<Integer,Integer> diceDistribution = new HashMap<>();
        for(int i = 0; i < rollNumber; i++) {
           totalSumOfRoll = getRollSum(diceNumber, sideNumber);
            if(diceDistribution.containsKey(totalSumOfRoll)) {
                diceDistribution.replace(totalSumOfRoll, diceDistribution.get(totalSumOfRoll) + 1);
            } else {
                diceDistribution.put(totalSumOfRoll, 1);
            }
        }
        saveDiceDistribution(diceNumber, sideNumber,rollNumber,diceDistribution);
        return diceDistribution;
    }


    private int getRollSum(int diceNumber, int sideNumber) {
        int totalSumOfRoll = 0;
        for(int i = 0; i < diceNumber; i++){
            totalSumOfRoll += random.ints(1, sideNumber + 1).findFirst().getAsInt();
        }
        return totalSumOfRoll;
    }

    private void saveDiceDistribution(int diceNumber, int sideNumber, int rollNumber, Map<Integer, Integer> diceDistribution) {
        DiceDistribution diceDistributionDbObject = new DiceDistribution();
        diceDistributionDbObject.setDiceNumber(diceNumber);
        diceDistributionDbObject.setSideNumber(sideNumber);
        diceDistributionDbObject.setRollNumber(rollNumber);
        diceDistributionDbObject.setDiceDistribution(diceDistribution);
        repository.save(diceDistributionDbObject);
    }

    public List<DiceSimulation> getDiceSimulations() {
        List<Dice.DiceAndSidesNumber> distinctDiceAndSidesNumberList;
        distinctDiceAndSidesNumberList = repository.findDistinctBy();
        List<DiceSimulation> diceSimulationList = new ArrayList<>();
        for(int i = 0; i < distinctDiceAndSidesNumberList.size(); i++) {
            Dice.DiceAndSidesNumber tmp = distinctDiceAndSidesNumberList.get(i);
            DiceSimulation diceSimulation = getDiceSimulation(tmp);
            diceSimulationList.add(diceSimulation);
        }
        return diceSimulationList;
    }

    private DiceSimulation getDiceSimulation(Dice.DiceAndSidesNumber diceAndSidesNumber) {
        int numberOfDice = diceAndSidesNumber.getDiceNumber();
        int numberOfDiceSides = diceAndSidesNumber.getSideNumber();
        DiceSimulation diceSimulation = new DiceSimulation();
        diceSimulation.setSideNumber(numberOfDiceSides);
        diceSimulation.setSimulationNumber(repository.countByDiceNumberAndSideNumber(numberOfDice, numberOfDiceSides));
        diceSimulation.setDiceNumber(numberOfDice);
        diceSimulation.setTotalNumberOfRolls(repository.sumTotalRollsByDiceNumberAndSideNumber(numberOfDice, numberOfDiceSides));
        return diceSimulation;
    }

    public Map<Integer, BigDecimal> getRelativeDistribution(int diceNumber, int sideNumber) {
        List<DiceDistribution> diceDistributionList = repository.findByDiceNumberAndSideNumber(diceNumber, sideNumber);
        Long totalNumberOfRolls = repository.sumTotalRollsByDiceNumberAndSideNumber(diceNumber, sideNumber);
        Map<Integer,Integer> sumOfDiceDistributions = new HashMap<>();
        Map<Integer, BigDecimal> relativeDistributions = new HashMap<>();
        for(DiceDistribution diceDistribution : diceDistributionList){
            diceDistribution.getDiceDistribution().forEach((key, value) -> sumOfDiceDistributions.merge(key,value, Integer::sum));
        }
        sumOfDiceDistributions.forEach((key, value) -> {
            BigDecimal percentage = new BigDecimal(value / (totalNumberOfRolls / 100.0));
            relativeDistributions.put(key, percentage.setScale(2, RoundingMode.HALF_EVEN));
        });
        return relativeDistributions;
    }

}
