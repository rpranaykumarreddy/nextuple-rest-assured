package org.example;


import org.testng.annotations.Test;
import java.util.List;
import static org.example.utils.TestUtils.*;

public class AllTestCases extends BaseTest{
    @Test
    public void createTenBookings(){
        int number = 10;
        List<BookingIds> createdBookings = loopCreateBookings(number);
        loopGetId(createdBookings);
        List<Bookings> updatedBookings = loopUpdateBookings(createdBookings,number);
        loopValidateUpdatedGetId(number,createdBookings,updatedBookings);
        loopDeleteBookings(createdBookings);
    }
}
