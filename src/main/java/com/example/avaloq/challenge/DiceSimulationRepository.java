package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.DiceSimulation;
import com.example.avaloq.challenge.model.DiceSimulationPrimaryKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiceSimulationRepository extends CrudRepository<DiceSimulation, DiceSimulationPrimaryKey> {
}
