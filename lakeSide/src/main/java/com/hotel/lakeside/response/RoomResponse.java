package com.hotel.lakeside.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {

    private Long id;

    private String roomType;

    private BigDecimal roomPrice;

    private boolean isBooked;

    private String foto;

    private List<BookingResponse> bookings;

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice,
                        boolean isBooked, byte[] fotoByte, List<BookingResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.foto = fotoByte != null ? Base64.encodeBase64String(fotoByte) : null;
        this.bookings = bookings;
    }


}
