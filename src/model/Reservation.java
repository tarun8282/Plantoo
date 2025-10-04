package model;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private int guestId;
    private String guestName;   // NEW: Useful for search results
    private int hotelId;
    private int roomNum;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public Reservation() {}

    public Reservation(int reservationId, int guestId, String guestName, int hotelId, int roomNum,
                       LocalDate checkIn, LocalDate checkOut, String status) {
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.guestName = guestName;
        this.hotelId = hotelId;
        this.roomNum = roomNum;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    // Getters & Setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getGuestId() { return guestId; }
    public void setGuestId(int guestId) { this.guestId = guestId; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    public int getRoomNum() { return roomNum; }
    public void setRoomNum(int roomNum) { this.roomNum = roomNum; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

}
