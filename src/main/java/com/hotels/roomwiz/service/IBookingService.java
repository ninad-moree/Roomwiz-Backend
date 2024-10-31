package com.hotels.roomwiz.service;

import java.util.List;

import com.hotels.roomwiz.model.BookedRoom;

public interface IBookingService {
    List<BookedRoom> getAllBookings();

    BookedRoom findByConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    void cancelBooking(Long bookingId);

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    List<BookedRoom> getBookingsByUserEmail(String email);
}
