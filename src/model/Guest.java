package model;

import java.util.List;

public class Guest {
    private int guestId;
    private String name;
    private int age;
    private String gender;
    private List<String> phoneNumbers; // multiple phones

    public Guest(int guestId, String name, int age, String gender, List<String> phoneNumbers) {
        this.guestId = guestId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phoneNumbers = phoneNumbers;
    }

    // Getters & Setters
    public int getGuestId() { return guestId; }
    public void setGuestId(int guestId) { this.guestId = guestId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public List<String> getPhoneNumbers() { return phoneNumbers; }
    public void setPhoneNumbers(List<String> phoneNumbers) { this.phoneNumbers = phoneNumbers; }

    // For TableView, sometimes we want a single string:
    public String getPhoneNumbersAsString() {
        return String.join(", ", phoneNumbers);
    }
}
