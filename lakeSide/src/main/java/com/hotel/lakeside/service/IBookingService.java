package com.hotel.lakeside.service;

import com.hotel.lakeside.model.BookedRoom;

import java.util.List;

public interface IBookingService {

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    List<BookedRoom> getAllRooms();

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    void cancelBooking(Long bookingId);
}
