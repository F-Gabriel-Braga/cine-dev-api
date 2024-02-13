package io.github.fgabrielbraga.CineDev.controller;

import io.github.fgabrielbraga.CineDev.dto.input.RoomInputDTO;
import io.github.fgabrielbraga.CineDev.dto.output.RoomOutputDTO;
import io.github.fgabrielbraga.CineDev.dto.output.SessionOutputDTO;
import io.github.fgabrielbraga.CineDev.service.RoomService;
import io.github.fgabrielbraga.CineDev.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private SessionService sessionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{uuid}")
    public ResponseEntity<?> findById(@PathVariable UUID uuid) {
        Optional<RoomOutputDTO> roomOpt = roomService.findById(uuid);
        return roomOpt
                .map(roomFound -> ResponseEntity.ok(roomFound))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) Short number) {
        List<RoomOutputDTO> rooms = roomService.findTop1000ByNumber(number);
        return ResponseEntity.ok(rooms);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/sessions/{uuid}")
    public ResponseEntity<?> findBySessionId(@PathVariable UUID uuid) {
        Optional<RoomOutputDTO> roomOpt = roomService.findBySessionId(uuid);
        return roomOpt
                .map(roomFound -> ResponseEntity.ok(roomFound))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody RoomInputDTO room) {
        RoomOutputDTO roomSaved = roomService.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomSaved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateDetails(
            @PathVariable UUID uuid,
            @RequestBody RoomInputDTO room) {
        Optional<RoomOutputDTO> roomOptional = roomService.findById(uuid);
        return roomOptional
                .map(roomFound -> {
                    room.setUuid(uuid);
                    RoomOutputDTO roomUpdated = roomService.updateDetails(room);
                    return ResponseEntity.ok(roomUpdated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{uuid}/maps")
    public ResponseEntity<?> updateSeatMap(
            @PathVariable UUID uuid,
            @RequestBody RoomInputDTO room) {
            Optional<RoomOutputDTO> roomOptional = roomService.findById(uuid);
            return roomOptional
                    .map(roomFound -> {
                        List<SessionOutputDTO> sessions = sessionService.findTop1000ByRoomId(uuid);
                        if(sessions.isEmpty()) {
                            room.setUuid(uuid);
                            RoomOutputDTO roomUpdated = roomService.updateSeatMap(room);
                            return ResponseEntity.ok(roomUpdated);
                        }
                        return ResponseEntity.badRequest().build();
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        Optional<RoomOutputDTO> roomOptional = roomService.findById(uuid);
        return roomOptional
                .map(roomFound -> {
                    roomService.deleteById(uuid);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
