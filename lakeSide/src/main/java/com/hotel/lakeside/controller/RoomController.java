package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.FotoRetrivalException;
import com.hotel.lakeside.model.BookedRoom;
import com.hotel.lakeside.model.Room;
import com.hotel.lakeside.response.BookingResponse;
import com.hotel.lakeside.response.RoomResponse;
import com.hotel.lakeside.service.BookedRoomImple;
import com.hotel.lakeside.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final IRoomService service;
    private final BookedRoomImple bookedService;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("foto") MultipartFile foto,
                                                   @RequestParam("roomType") String roomType,
                                                   @RequestParam("roomPrice") BigDecimal roomPrice)
            throws SQLException, IOException {

        Room saveRoom = service.addNewRoom(foto,roomType,roomPrice);
        RoomResponse roomResponse = new RoomResponse(saveRoom.getId(), saveRoom.getRoomType(),saveRoom.getRoomPrice());
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes(){
        return service.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = service.getAllRoms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms){
            byte[] fotoBytes = service.getRoomFotoByRoomId(room.getId());
            if(fotoBytes != null && fotoBytes.length > 0) {
                String base64Foto = Base64.encodeBase64String(fotoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setFoto(base64Foto);
                roomResponses.add(roomResponse);
            }
        }
        return ResponseEntity.ok(roomResponses);
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings.stream()
                .map(bookedRoom -> new BookingResponse(bookedRoom.getBookingId(),
                        bookedRoom.getCheckInDate(), bookedRoom.getCheckOutDate(),
                        bookedRoom.getBookingConfirmationCode())).toList();

        byte[] fotoBytes = null;
        Blob fotoBlob = room.getFoto();
        if (fotoBlob != null){
            try {
                fotoBytes = fotoBlob.getBytes(1, (int) fotoBlob.length());;
            }catch (SQLException e){
                throw new FotoRetrivalException("Error retrieving foto");
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(),
                room.getRoomPrice(),room.isBooked(), fotoBytes, bookingInfo);
    }

    private List<BookedRoom> getBookingsByRoomId(Long roomId) {
        return bookedService.getAllBookingsByRoomId(roomId);
    }
}
