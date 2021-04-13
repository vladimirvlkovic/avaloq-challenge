package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.DiceDistribution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

}
