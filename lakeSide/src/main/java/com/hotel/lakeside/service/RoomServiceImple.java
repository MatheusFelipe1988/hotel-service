package com.hotel.lakeside.service;

import com.hotel.lakeside.exception.InternalServerException;
import com.hotel.lakeside.exception.ResourceNotFoundException;
import com.hotel.lakeside.model.Room;
import com.hotel.lakeside.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImple implements IRoomService{

    private final RoomRepository repository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType,
                           BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()) {
            byte[] fotoByte = file.getBytes();
            Blob fotoBlob = new SerialBlob(fotoByte);
            room.setFoto(fotoBlob);
        }

        return repository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return repository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRoms() {
        return repository.findAll();
    }

    @Override
    public byte[] getRoomFotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> theRoom = repository.findById(roomId);
        if (theRoom.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, not found room");
        }
        Blob fotoBlob = theRoom.get().getFoto();
        if (fotoBlob != null) {
            return fotoBlob.getBytes(1, (int) fotoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = repository.findById(roomId);
        if (theRoom.isPresent()) {
            repository.deleteById(roomId);
        }

    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] fotoBytes) {
        Room room = repository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (fotoBytes != null && fotoBytes.length > 0) {
            try {
                room.setFoto(new SerialBlob(fotoBytes));
            } catch (SQLException e) {
                throw new InternalServerException("Error updating room");
            }
        }
        return repository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(repository.findById(roomId).get());
    }

    @Override
    public List<Room> getAvailableRoom(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return repository.findAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
    }
}
