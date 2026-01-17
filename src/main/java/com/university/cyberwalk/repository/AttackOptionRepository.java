package com.university.cyberwalk.repository;

import com.university.cyberwalk.model.AttackOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttackOptionRepository extends JpaRepository<AttackOption, Long> {
    List<AttackOption> findByAttackScenarioId(Long attackScenarioId);
}
