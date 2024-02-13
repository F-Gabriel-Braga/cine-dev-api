package io.github.fgabrielbraga.CineDev.repository;

import io.github.fgabrielbraga.CineDev.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query(value = "SELECT * FROM rooms WHERE " +
            "room_number = IFNULL(?, room_number) " +
            "ORDER BY room_number", nativeQuery = true)
    List<Room> findByRoomNumber(Short number);

    @Query(value = "SELECT r.* FROM rooms r " +
            "JOIN sessions s ON s.room_uuid = r.uuid " +
            "WHERE s.uuid = ? " +
            "ORDER BY r.room_number", nativeQuery = true)
    Optional<Room> findBySessionId(UUID uuid);

    List<Room> findAllByOrderByNumber();
}
