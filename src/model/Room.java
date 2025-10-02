package model;

public class Room {
    private int hotelId;
    private int roomNum;
    private int roomTypeId;
    private String roomTypeName;
    private double rent;
    private String status;
    private String hotelName; // For displaying in TableView

    public Room(int hotelId, int roomNum, int roomTypeId, String roomTypeName, double rent, String status, String hotelName) {
        this.hotelId = hotelId;
        this.roomNum = roomNum;
        this.roomTypeId = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.rent = rent;
        this.status = status;
        this.hotelName = hotelName;
    }

    public int getHotelId() { return hotelId; }
    public int getRoomNum() { return roomNum; }
    public int getRoomTypeId() { return roomTypeId; }
    public String getRoomTypeName() { return roomTypeName; }
    public double getRent() { return rent; }
    public String getStatus() { return status; }
    public String getHotelName() { return hotelName; }

    public void setStatus(String status) { this.status = status; }
    public void setRoomTypeId(int roomTypeId) { this.roomTypeId = roomTypeId; }
    public void setRent(double rent) { this.rent = rent; }
}
