package com.example.avaloq.challenge;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DiceDistributionController {
    final DiceDistributionService diceDistributionService;

    @GetMapping("api/dice-distribution")
    public Map<Integer,Integer> getDiceDistributionResult() {
        return diceDistributionService.getDiceDistribution();
    }
}
