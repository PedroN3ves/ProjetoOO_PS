package model;

public class Booking
{
    private String email;
    private String hotelName;
    private String roomNumber;

    public Booking(String email, String hotelName, String roomNumber)
    {
        this.email = email;
        this.hotelName = hotelName;
        this.roomNumber = roomNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public String getRoomNumber()
    {
        return roomNumber;
    }
}
