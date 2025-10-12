package model;

public class Department {
    private int deptId;
    private String deptName;
    private String hotelName;

    public Department(int deptId, String deptName, String hotelName) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.hotelName = hotelName;
    }

    public int getDeptId() { return deptId; }
    public String getDeptName() { return deptName; }
    public String getHotelName() { return hotelName; }

    public void setDeptId(int deptId) { this.deptId = deptId; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
}
