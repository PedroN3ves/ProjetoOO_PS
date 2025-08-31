package manager;

import model.Room;
import model.Customer;
import model.Booking;
import util.PaymentProcessor;
import util.LanguageManager;
import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BookingManager
{
    private List<Booking> bookings = new ArrayList<>();
    private Scanner scanner;
    private CustomerManager customerManager;
    private RoomManager roomManager;

    public BookingManager(Scanner scanner, CustomerManager customerManager, RoomManager roomManager)
    {
        this.scanner = scanner;
        this.customerManager = customerManager;
        this.roomManager = roomManager;
    }

    public void bookRoom()
    {
        System.out.println(LanguageManager.getMessage("booking.customer_email"));
        String email = scanner.nextLine();
        Customer customer = customerManager.getCustomerByEmail(email);
        if (customer == null)
        {
            System.out.println(LanguageManager.getMessage("booking.customer_not_found"));
            return;
        }

        System.out.println(LanguageManager.getMessage("booking.hotel_name"));
        String hotelName = scanner.nextLine();
        System.out.println(LanguageManager.getMessage("booking.room_number"));
        String roomNumber = scanner.nextLine();

        Room room = roomManager.getAvailableRoom(hotelName, roomNumber);
        if (room == null)
        {
            System.out.println(LanguageManager.getMessage("booking.room_not_available"));
            return;
        }

        double amount = room.getPrice();
        boolean paid = PaymentProcessor.processPayment(customer.getName(), amount);
        if (!paid)
        {
            System.out.println(LanguageManager.getMessage("booking.payment_failed"));
            return;
        }

        room.setAvailable(false);
        bookings.add(new Booking(email, hotelName, roomNumber));
        customerManager.addLoyaltyPoints(email, 10);
        System.out.println(MessageFormat.format(LanguageManager.getMessage("booking.success"), roomNumber, hotelName, customerManager.getLoyaltyPoints(email)));
    }

    public void cancelBooking()
    {
        System.out.println(LanguageManager.getMessage("booking.cancel_hotel"));
        String hotelName = scanner.nextLine();
        System.out.println(LanguageManager.getMessage("booking.cancel_room"));
        String roomNumber = scanner.nextLine();

        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext())
        {
            Booking b = iterator.next();
            if (b.getHotelName().equalsIgnoreCase(hotelName) && b.getRoomNumber().equals(roomNumber))
            {
                Room room = roomManager.getRoom(hotelName, roomNumber);
                if (room != null)
                {
                    room.setAvailable(true);
                }
                iterator.remove();
                System.out.println(LanguageManager.getMessage("booking.cancelled"));
                return;
            }
        }
        System.out.println(LanguageManager.getMessage("booking.not_found"));
    }
}
