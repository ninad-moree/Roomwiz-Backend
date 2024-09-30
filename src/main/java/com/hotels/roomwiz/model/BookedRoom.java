package com.hotels.roomwiz.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "check_In")
    private LocalDate checkInDate;

    @Column(name = "check_Out")
    private LocalDate checkOutDate;

    @Column(name = "guest_FullName")
    private String guestFullName;

    @Column(name = "guest_Email")
    private String questEmail;

    @Column(name = "adults")
    private int numOfAdults;

    @Column(name = "children")
    private int numOfChildren;

    @Column(name = "total_guest")
    private int totalNumberOfGuests;

    @Column(name = "confirmation_Code")
    private String bookingConfirmationCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalNumberOfGuests() {
        this.totalNumberOfGuests = this.numOfAdults + this.numOfChildren;
    }
    
    public void setNumOfAdults(int adults) {
        numOfAdults = adults;
        calculateTotalNumberOfGuests();
    }

    public void setNumOfChildren(int children) {
        numOfChildren = children;
        calculateTotalNumberOfGuests();
    }

    public void setBookingConfirmationCode(String confirmationCode) {
        bookingConfirmationCode = confirmationCode;
    }
}
