package com.example.avaloq.challenge;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
public class DiceDistributionController {
    final DiceDistributionService diceDistributionService;

    @GetMapping("api/dice-distribution")
    public Map<Integer, Integer> getDiceDistributionResult(@RequestParam(required = false, defaultValue = "3") @Min(1) Integer numberOfDice,
                                                           @RequestParam(required = false, defaultValue = "6") @Min(4) Integer numberOfDiceSides,
                                                           @RequestParam(required = false, defaultValue = "100") @Min(1) Integer numberOfRolls) {
        return diceDistributionService.getDiceDistribution(numberOfDice, numberOfDiceSides, numberOfRolls);
    }
}
