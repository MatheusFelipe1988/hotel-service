package com.hotel.lakeside.service;

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
        if (!file.isEmpty()){
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
        if(theRoom.isEmpty()){
            throw new ResourceNotFoundException("Sorry, not found room");
        }
        Blob fotoBlob = theRoom.get().getFoto();
        if (fotoBlob != null){
            return  fotoBlob.getBytes(1, (int) fotoBlob.length());
        }
        return null;
    }
}
