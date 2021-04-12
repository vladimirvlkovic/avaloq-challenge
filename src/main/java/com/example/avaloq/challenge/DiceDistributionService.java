package com.example.avaloq.challenge;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class DiceDistributionService {

    Random random = new Random();

    public Map<Integer,Integer> getDiceDistribution(int numberOfDice, int numberOfDiceSides, int numberOfRoles) {
        int totalSumOfRoll;
        Map<Integer,Integer> diceDistribution = new HashMap<>();
        for(int i = 0; i < numberOfRoles; i++) {
           totalSumOfRoll = getRollSum(numberOfDice, numberOfDiceSides);
            if(diceDistribution.containsKey(totalSumOfRoll)) {
                diceDistribution.replace(totalSumOfRoll, diceDistribution.get(totalSumOfRoll) + 1);
            } else {
                diceDistribution.put(totalSumOfRoll, 1);
            }
        }
        return diceDistribution;
    }

    private int getRollSum(int numberOfDice, int numberOfDiceSides) {
        int totalSumOfRoll = 0;
        for(int i = 0; i < numberOfDice; i++){
            totalSumOfRoll += random.ints(1, numberOfDiceSides + 1).findFirst().getAsInt();
        }
        return totalSumOfRoll;
    }

}
