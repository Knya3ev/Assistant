package com.example.assistant.repository;

import com.example.assistant.model.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    @Modifying
    @Query(value = """
            UPDATE Scenario 
            SET is_run = :isRun
            WHERE id = :scenarioId
            """, nativeQuery = true)
    void toggleIsRunnable(@Param("scenarioId") Long scenarioId, @Param("isRun") boolean isRun);

    Optional<Scenario> findByName(String chatAssistant);
}
