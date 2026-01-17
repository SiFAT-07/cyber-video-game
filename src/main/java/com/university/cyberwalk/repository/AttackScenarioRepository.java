package com.university.cyberwalk.repository;

import com.university.cyberwalk.model.AttackScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttackScenarioRepository extends JpaRepository<AttackScenario, Long> {
    List<AttackScenario> findByLevelId(Long levelId);
}
