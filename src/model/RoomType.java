package model;

public class RoomType {
    private int roomTypeId;
    private String typeName;
    private double rent;

    public RoomType(int roomTypeId, String typeName, double rent) {
        this.roomTypeId = roomTypeId;
        this.typeName = typeName;
        this.rent = rent;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return typeName; // useful for ComboBox display
    }
}
