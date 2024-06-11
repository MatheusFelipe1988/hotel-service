package com.hotel.lakeside.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkinOutDate;
    private String guestName;
    private String guestEmail;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfGuests;
    private String bookingConfirmationCOde;
    private RoomResponse room;

    public BookingResponse(LocalDate checkinOutDate, LocalDate checkInDate, Long id,
                           String bookingConfirmationCOde) {
        this.checkinOutDate = checkinOutDate;
        this.checkInDate = checkInDate;
        this.id = id;
        this.bookingConfirmationCOde = bookingConfirmationCOde;
    }
}
