package com.university.cyberwalk.repository;

import com.university.cyberwalk.model.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
    Optional<GameRoom> findByRoomId(String roomId);
}
