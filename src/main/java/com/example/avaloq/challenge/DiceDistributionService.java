package com.example.avaloq.challenge;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class DiceDistributionService {

    Random random = new Random();

    public Map<Integer,Integer> getDiceDistribution() {
        int total;
        int min = 1;
        int max = 6;
        Map<Integer,Integer> diceDistribution = new HashMap<>();
        for(int i = 0; i < 100; i++) {
            total = 0;
            for(int j=0; j<3; j++){
                total += random.ints(1, 7).findFirst().getAsInt();
            }
            if(diceDistribution.containsKey(total)) {
                diceDistribution.replace(total, diceDistribution.get(total) + 1);
            } else {
                diceDistribution.put(total, 1);
            }
        }
        return diceDistribution;
    }

}
