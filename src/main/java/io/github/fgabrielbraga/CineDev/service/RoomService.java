package io.github.fgabrielbraga.CineDev.service;

import io.github.fgabrielbraga.CineDev.dto.input.RoomInputDTO;
import io.github.fgabrielbraga.CineDev.dto.output.RoomOutputDTO;
import io.github.fgabrielbraga.CineDev.enums.AreaType;
import io.github.fgabrielbraga.CineDev.exceptions.ResourceNotFoundException;
import io.github.fgabrielbraga.CineDev.exceptions.ResourceUnavailableException;
import io.github.fgabrielbraga.CineDev.model.Room;
import io.github.fgabrielbraga.CineDev.model.Session;
import io.github.fgabrielbraga.CineDev.repository.RoomRepository;
import io.github.fgabrielbraga.CineDev.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private SessionRepository sessionRepository;

    public RoomOutputDTO findById(UUID uuid) {
        Optional<Room> roomOpt = roomRepository.findById(uuid);
        return roomOpt.map(RoomOutputDTO::ofRoom).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, sala não encontrada. Tente novamente."));
    }

    public RoomOutputDTO findBySessionId(UUID uuid) {
        Optional<Room> roomOpt = roomRepository.findBySessionId(uuid);
        return roomOpt.map(RoomOutputDTO::ofRoom).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, sala não encontrada. Tente novamente."));
    }

    public List<RoomOutputDTO> findTop1000() {
        List<Room> rooms = roomRepository.findTop1000ByOrderByNumber();
        rooms.stream().forEach(room -> room.getMap().getAreas().clear());
        return rooms.stream().map(RoomOutputDTO::ofRoom).toList();
    }

    public List<RoomOutputDTO> findTop1000ByNumber(Short number) {
        List<Room> rooms = roomRepository.findTop1000ByNumber(number);
        rooms.stream().forEach(room -> room.getMap().getAreas().clear());
        return rooms.stream().map(RoomOutputDTO::ofRoom).toList();
    }

    public RoomOutputDTO save(RoomInputDTO roomDTO) {
        Room room = RoomInputDTO.parseRoom(roomDTO);
        room.setCapacity((short) room.getMap().getAreas().stream().filter(area -> {
            return area.getAreaType() == AreaType.SEAT;
        }).count());
        room.getMap().getAreas().stream().forEach(area -> {
            area.setMap(room.getMap());
        });
        resetIdentifier(room);
        Room roomSaved = roomRepository.save(room);
        return RoomOutputDTO.ofRoom(roomSaved);
    }

    public RoomOutputDTO updateDetails(RoomInputDTO roomDTO) {
        Optional<Room> roomOpt = roomRepository.findById(roomDTO.getUuid());
        return roomOpt.map(roomFound -> {
            roomFound.setNumber(roomDTO.getNumber());
            roomFound.setProjectionType(roomDTO.getProjectionType());
            Room roomSaved = roomRepository.save(roomFound);
            return RoomOutputDTO.ofRoom(roomSaved);
        }).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, sala não encontrada. Tente novamente."));
    }

    public RoomOutputDTO updateSeatMap(RoomInputDTO roomDTO) {
        Optional<Room> roomOpt = roomRepository.findById(roomDTO.getUuid());
        return roomOpt.map(roomFound -> {
            List<Session> sessions = sessionRepository.findTop1000ByRoomId(roomFound.getUuid());
            if(sessions.isEmpty()) {
                Room roomEdited = RoomInputDTO.parseRoom((roomDTO));
                roomEdited.setCapacity((short) roomEdited.getMap().getAreas().stream().filter(area -> {
                    return area.getAreaType() == AreaType.SEAT;
                }).count());
                roomFound.setMap(roomEdited.getMap());
                roomFound.setCapacity(roomEdited.getCapacity());
                roomFound.getMap().getAreas().stream().forEach(area -> {
                    area.setMap(roomFound.getMap());
                });
                Room roomSaved = roomRepository.save(roomFound);
                return RoomOutputDTO.ofRoom(roomSaved);
            }
            throw new ResourceUnavailableException("Desculpe, esta sala tem sessões abertas. Tente novamente.");
        }).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, sala não encontrada. Tente novamente."));
    }

    public void deleteById(UUID uuid) {
        Optional<Room> roomOpt = roomRepository.findById(uuid);
        roomOpt.orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, sala não encontrada. Tente novamente."));
        roomRepository.deleteById(uuid);
    }

    private void resetIdentifier(Room room) {
        room.setUuid(null);
        room.getMap().setUuid(null);
        room.getMap().getAreas().forEach(area -> area.setUuid(null));
    }
}
