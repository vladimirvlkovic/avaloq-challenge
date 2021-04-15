package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.DiceSimulation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Constraint;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleException(ConstraintViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationException(exception.getMessage()));
    }
     public class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
            log.warn(message);
        }
         @Override
         public synchronized Throwable fillInStackTrace() {
             return this;
         }
     }
}
