package com.hotel.lakeside.service;

import com.hotel.lakeside.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public interface IRoomService {
    Room addNewRoom(MultipartFile foto, String roomType, BigDecimal roomPrice) throws SQLException, IOException;
}
