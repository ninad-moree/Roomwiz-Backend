package com.hotels.roomwiz.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.hotels.roomwiz.exception.PhotoRetrievalException;
import com.hotels.roomwiz.exception.ResourceNotFoundException;
// import com.hotels.roomwiz.model.BookedRoom;
import com.hotels.roomwiz.model.Room;
// import com.hotels.roomwiz.response.BookingResponse;
import com.hotels.roomwiz.response.RoomResponse;
// import com.hotels.roomwiz.service.BookedRoomServiceImpl;
import com.hotels.roomwiz.service.IRoomService;

import io.jsonwebtoken.io.SerialException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@CrossOrigin(origins = "*") 
public class RoomController {
    private final IRoomService roomService;
    // private final BookedRoomServiceImpl bookedRoomService;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile photo, @RequestParam("roomType") String roomType, @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException, SerialException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/room-types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/get-rooms")
    public ResponseEntity<List<RoomResponse>>  getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();

        for(Room r : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(r.getId());
            if(photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(r);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);
            }
        }

        return ResponseEntity.ok(roomResponses);
    }

    private RoomResponse getRoomResponse(Room room) {
        // List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        // List<BookingResponse> bookingResponses = bookings
        //         .stream()
        //         .map(booking -> new BookingResponse(
        //                 booking.getBookingId(), 
        //                 booking.getCheckInDate(), 
        //                 booking.getCheckOutDate(),
        //                 booking.getBookingConfirmationCode()
        //         )).toList();

        byte[] photoBytes = null;
        Blob  photoBlob = room.getPhoto();
        if(photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes);
    }

    // private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
    //     return bookedRoomService.getAllBookingsByRoomId(roomId);
    // }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/edit/{roomId}") 
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId, @RequestParam(required = false) String roomType, @RequestParam(required = false) BigDecimal roomPrice, @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        theRoom.setPhoto(photoBlob);
        RoomResponse roomResponse = getRoomResponse(theRoom);
        return ResponseEntity.ok(roomResponse);
    } 

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> {
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room Not Found."));
    }
}
