package model;

public class Hotel {
    private int hotelId;       // auto-incremented in DB
    private String hotelName;
    private int totalRooms;
    private String location;

    // Default constructor
    public Hotel() {
    }

    // Constructor for adding new hotel (without ID)
    public Hotel(String hotelName, int totalRooms, String location) {
        this.hotelName = hotelName;
        this.totalRooms = totalRooms;
        this.location = location;
    }

    // Constructor for editing existing hotel (with ID)
    public Hotel(int hotelId, String hotelName, int totalRooms, String location) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.totalRooms = totalRooms;
        this.location = location;
    }

    // Getters & Setters
    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
