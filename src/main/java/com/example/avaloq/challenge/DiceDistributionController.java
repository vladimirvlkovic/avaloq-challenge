package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.DiceSimulation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
public class DiceDistributionController {
    final DiceDistributionService diceDistributionService;

    @GetMapping("api/dice-distribution")
    public Map<Integer, Integer> getDiceDistributionResult(@RequestParam(required = false, defaultValue = "3") @Min(1) Integer diceNumber,
                                                           @RequestParam(required = false, defaultValue = "6") @Min(4) Integer sideNumber,
                                                           @RequestParam(required = false, defaultValue = "100") @Min(1) Integer rollNumber) {
        return diceDistributionService.getDiceDistribution(diceNumber, sideNumber, rollNumber);
    }
    @GetMapping("api/dice-simulation-list")
    public List<DiceSimulation> getDiceSimulationList() {
        return diceDistributionService.getDiceSimulations();
    }

    @GetMapping("api/relative-dice-distribution")
    public Map<Integer, BigDecimal> getRelativeDiceDistribution(@RequestParam(required = false, defaultValue = "3") @Min(1) Integer diceNumber,
                                                                @RequestParam(required = false, defaultValue = "6") @Min(4) Integer sideNumber) {
        return diceDistributionService.getRelativeDistribution(diceNumber, sideNumber);
    }
}
