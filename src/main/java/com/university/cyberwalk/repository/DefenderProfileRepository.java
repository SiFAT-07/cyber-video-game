package com.university.cyberwalk.repository;

import com.university.cyberwalk.model.DefenderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefenderProfileRepository extends JpaRepository<DefenderProfile, Long> {
    List<DefenderProfile> findByLevelId(Long levelId);
}
