package com.hotel.lakeside.service;

import com.hotel.lakeside.exception.InvalidBookingRequestException;
import com.hotel.lakeside.exception.ResourceNotFoundException;
import com.hotel.lakeside.model.BookedRoom;
import com.hotel.lakeside.model.Room;
import com.hotel.lakeside.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService{

    private final IRoomService roomService;
    private final BookingRepository repository;


    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return repository.findByRoomId(roomId);
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return repository.findAll();
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return repository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new
                ResourceNotFoundException("No booking found with booking code"));
    }

    @Override
    public void cancelBooking(Long bookingId) {
        repository.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in in data must come before check-out data");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailble(bookingRequest, existingBookings);
        if (roomIsAvailable){
            room.addBooking(bookingRequest);
            repository.save(bookingRequest);
        }else {
            throw new InvalidBookingRequestException("Sorry, this room is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    private boolean roomIsAvailble(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

}
