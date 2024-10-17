package com.hotels.roomwiz.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotels.roomwiz.exception.InvalidBookingRequestException;
import com.hotels.roomwiz.exception.ResourceNotFoundException;
import com.hotels.roomwiz.model.BookedRoom;
import com.hotels.roomwiz.model.Room;
import com.hotels.roomwiz.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    
    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public BookedRoom findByConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new ResourceNotFoundException("No Booking found."));
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Check-in date should be before checout-date.");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        
        boolean isRoomAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if(isRoomAvailable) {
            room.addBookings(bookingRequest);
            bookingRepository.save(bookingRequest);
        } else {
            throw new InvalidBookingRequestException("This room is already booked.");
        }

        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
            bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate()) ||
            bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate()) ||
            (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate()) && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())) ||
            (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()) && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())) ||
            (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()) && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate())) ||
            (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())) ||
            (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
        );
    }
}
