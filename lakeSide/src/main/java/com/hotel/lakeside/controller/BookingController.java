package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.InvalidBookingRequestException;
import com.hotel.lakeside.exception.ResourceNotFoundException;
import com.hotel.lakeside.model.BookedRoom;
import com.hotel.lakeside.model.Room;
import com.hotel.lakeside.response.BookingResponse;
import com.hotel.lakeside.response.RoomResponse;
import com.hotel.lakeside.service.IBookingService;
import com.hotel.lakeside.service.IRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RestController
@Tag(name = "Reserva de Quarto")
@RequestMapping("/bookings")
public class BookingController {

    private final IBookingService service;
    private final IRoomService roomService;


    @Operation(summary = "Lista reservas", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listando todas as reservas"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookedRooms = service.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedRoom booking : bookedRooms){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @Operation(summary = "Busca reserva por ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID da reserva encontrado"),
            @ApiResponse(responseCode = "400", description = "Não existe este ID")
    })
    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingByUserEmail(@PathVariable String email){
        List<BookedRoom> bookings = service.getBookingsByUserEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom bookedRoom : bookings){
            BookingResponse bookingResponse = getBookingResponse(bookedRoom);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @Operation(summary = "Nova reserva", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inserindo nova reserva"),
            @ApiResponse(responseCode = "400", description = "Erro na criação")
    })
    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest){
        try {
            String confirmationCode = service.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Room boked successfully, your booking confirmation code is: "
                    + confirmationCode);
        }catch (InvalidBookingRequestException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @Operation(summary = "Buscando Codigo", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "busca com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro na busca")
    })
    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingConfirmationCode(@PathVariable String confirmationCode){
        try {
            BookedRoom booking = service.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Deletando reservas", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remoção bem sucedida"),
            @ApiResponse(responseCode = "400", description = "erro ao deletar reservas")
    })
    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        service.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedRoom booking){
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(
                theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice());
        return new BookingResponse(
                booking.getBookingId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getBookingConfirmationCode(),
                booking.getGuestEmail(),
                booking.getNumberOfAdults(),
                booking.getNumberOfAdults(),
                booking.getTotalFullguest(),
                booking.getGuestFullName(), room);
    }
}