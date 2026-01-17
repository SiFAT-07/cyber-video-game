package com.university.cyberwalk.repository;

import com.university.cyberwalk.model.DefenderChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefenderChoiceRepository extends JpaRepository<DefenderChoice, Long> {
    List<DefenderChoice> findByAttackOptionId(Long attackOptionId);
}
