package com.hotel.lakeside.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "check_in")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "guest_fullname")
    private String guestFullName;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "adults")
    private int NumberOfAdults;

    @Column(name = "children")
    private int NumberOfChildren;

    @Column(name = "total_guest")
    private int totalFullguest;

    @Column(name = "confirmation_code")
    private String bookingConfirmationCode;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    public void totalCalculateNumberOfGuest(){
        this.totalFullguest = this.NumberOfAdults + NumberOfChildren;
    }

   public  void setNumberOfAdults(int numberOfAdults){
        NumberOfAdults = numberOfAdults;
        totalCalculateNumberOfGuest();

   }

    public void setNumberOfChildren(int numberOfChildren) {
        NumberOfChildren = numberOfChildren;
        totalCalculateNumberOfGuest();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode){
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
