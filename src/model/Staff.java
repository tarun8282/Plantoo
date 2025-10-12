package model;

public class Staff {
    private int staffId;
    private String name;
    private String gender;
    private double salary;
    private String department;
    private String hotel;
    private String phone;

    public Staff(int staffId, String name, String gender, double salary, String department, String hotel, String phone) {
        this.staffId = staffId;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
        this.department = department;
        this.hotel = hotel;
        this.phone = phone;
    }

    // Getters and setters
    public int getStaffId() { return staffId; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public double getSalary() { return salary; }
    public String getDepartment() { return department; }
    public String getHotel() { return hotel; }
    public String getPhone() { return phone; }
}
