package com.hotel.lakeside.controller;

import com.hotel.lakeside.model.Room;
import com.hotel.lakeside.response.RoomResponse;
import com.hotel.lakeside.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final IRoomService service;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("foto")MultipartFile foto, @RequestParam("roomType")
                                                   String roomType, @RequestParam("roomPrice")BigDecimal roomPrice)
            throws SQLException, IOException {

        Room saveRoom = service.addNewRoom(foto,roomType,roomPrice);
        RoomResponse roomResponse = new RoomResponse(saveRoom.getId(), saveRoom.getRoomType(),saveRoom.getRoomPrice());
        return ResponseEntity.ok(roomResponse);
    }
}
