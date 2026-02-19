package com.university.cyberwalk.repository;

import com.university.cyberwalk.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    List<Level> findByEnabledTrueOrderByOrderIndexAsc();

    List<Level> findAllByOrderByOrderIndexAsc();
}
