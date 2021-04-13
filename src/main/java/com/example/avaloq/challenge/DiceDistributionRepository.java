package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.DiceDistribution;
import com.example.avaloq.challenge.model.DiceSimulationPrimaryKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiceDistributionRepository extends CrudRepository<DiceDistribution, Long> {

    List<DiceDistribution> findByNumberOfDiceAndNumberOfDiceSides(int numberOfDice, int numberOfDiceSides);
}
