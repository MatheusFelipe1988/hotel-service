package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.FotoRetrivalException;
import com.hotel.lakeside.exception.ResourceNotFoundException;
import com.hotel.lakeside.model.BookedRoom;
import com.hotel.lakeside.model.Room;
import com.hotel.lakeside.response.BookingResponse;
import com.hotel.lakeside.response.RoomResponse;
import com.hotel.lakeside.service.BookingServiceImpl;
import com.hotel.lakeside.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final IRoomService service;
    private final BookingServiceImpl bookedService;

    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("foto") MultipartFile foto,
                                                   @RequestParam("roomType") String roomType,
                                                   @RequestParam("roomPrice") BigDecimal roomPrice)
            throws SQLException, IOException {

        Room saveRoom = service.addNewRoom(foto, roomType, roomPrice);
        RoomResponse roomResponse = new RoomResponse(saveRoom.getId(), saveRoom.getRoomType(), saveRoom.getRoomPrice());
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/room/types")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public List<String> getRoomTypes() {
        return service.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = service.getAllRoms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            byte[] fotoBytes = service.getRoomFotoByRoomId(room.getId());
            if (fotoBytes != null && fotoBytes.length > 0) {
                String base64Foto = Base64.encodeBase64String(fotoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setFoto(base64Foto);
                roomResponses.add(roomResponse);
            }
        }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        service.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile foto)
            throws IOException, SQLException {
        byte[] fotoBytes = foto != null && !foto.isEmpty() ? foto.getBytes() : service.getRoomFotoByRoomId(roomId);
        Blob fotoBlob = fotoBytes != null && fotoBytes.length > 0 ? new SerialBlob(fotoBytes) : null;
        Room theRoom = service.updateRoom(roomId, roomType, roomPrice, fotoBytes);
        theRoom.setFoto(fotoBlob);
        RoomResponse roomResponse = getRoomResponse(theRoom);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/room/{roomId}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
        Optional<Room> theRoom = service.getRoomById(roomId);
        return theRoom.map(room -> {
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @GetMapping("/available-rooms")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("roomType") String roomType
            ) throws SQLException {
        List<Room> availableRoom = service.getAvailableRoom(checkInDate,checkOutDate, roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : availableRoom){
            byte[] fotoBytes = service.getRoomFotoByRoomId(room.getId());
            if(fotoBytes != null && fotoBytes.length > 0){
                String fotoBase64 = Base64.encodeBase64String(fotoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setFoto(fotoBase64);
                roomResponses.add(roomResponse);
            }
        }
        if(roomResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(roomResponses);
        }
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings.stream()
                .map(bookedRoom -> new BookingResponse(bookedRoom.getBookingId(),
                        bookedRoom.getCheckInDate(), bookedRoom.getCheckOutDate(),
                        bookedRoom.getBookingConfirmationCode())).toList();
        byte[] fotoBytes = null;
        Blob fotoBlob = room.getFoto();
        if (fotoBlob != null) {
            try {
                fotoBytes = fotoBlob.getBytes(1, (int) fotoBlob.length());
                ;
            } catch (SQLException e) {
                throw new FotoRetrivalException("Error retrieving foto");
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(),
                room.getRoomPrice(), room.isBooked(), fotoBytes, bookingInfo);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookedService.getAllBookingsByRoomId(roomId);
    }
}
