package com.example.booking_system.room;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository){
        this.roomRepository=roomRepository;
    }

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id){
        return roomRepository.findById(id);
    }

    public List<Room> getAvailableRooms(){
        return roomRepository.findByIsAvailableTrue();
    }

    public Room saveRoom(Room room){
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id){
        roomRepository.deleteById(id);
    }

    public List<RoomDTO> getAllRoomDTOs() {
        return roomRepository.findAll().stream()
                .map(room -> new RoomDTO(
                        room.getId(),
                        room.getName(),
                        room.getDescription(),
                        room.getCapacity(),
                        room.getLocation(),
                        room.isAvailable()
                ))
                .toList();
    }

}
