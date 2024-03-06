package org.example;

import com.beust.ah.A;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

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

    private void loopDeleteBookings(List<BookingIds> createdBookings) {
        for(BookingIds bookingIds: createdBookings){
            int bookingId = bookingIds.getBookingid();
            Response responseDelete = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
                    .delete("/booking/" + bookingId);
            responseDelete.print();
            assertEquals(responseDelete.getStatusCode(), 201, "StatusCode not as expected");

            Response responseGet = RestAssured.get("https://restful-booker.herokuapp.com/booking/" + bookingId);
            responseGet.print();
            assertEquals(responseGet.getBody().asString(), "Not Found", "Body not as expected");
        }
    }

    //Loops
    private List<BookingIds> loopCreateBookings(int number){
        List<BookingIds> listReturn = new ArrayList<>();
        for(int j=1;j<=number;j++){
            listReturn.add(createBookings(j));
        }
        return listReturn;
    }

    private void loopGetId(List<BookingIds> createdBookings) {
        for(BookingIds booking:createdBookings){
            validateIdBooking(booking);
        }
    }
    private List<Bookings> loopUpdateBookings(List<BookingIds> createdBookings, int number) {
        List<Bookings> listReturn = new ArrayList<>();
        for(int j=0;j<number;j++){
            listReturn.add(updateBookings(j, createdBookings.get(j)));
        }
        return listReturn;
    }

    private void loopValidateUpdatedGetId(int number, List<BookingIds> createdBookings, List<Bookings> updatedBookings) {
        for(int i=0;i<number;i++){
            BookingIds bookingId = createdBookings.get(i);
            Bookings updatedBooking = updatedBookings.get(i);
            int id = bookingId.getBookingid();

            Response response = RestAssured.given(spec).get("/booking/" + String.valueOf(id));
            response.print();

            Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
            SoftAssert softAssert = new SoftAssert();
            JsonPath act= response.jsonPath();
            softAssert.assertEquals(act.getString("firstname"), updatedBooking.getFirstname(), "firstname not as expected");
            softAssert.assertEquals(act.getString("lastname"), updatedBooking.getLastname(), "lastname not as expected");
            softAssert.assertEquals(act.getInt("totalprice"), updatedBooking.getTotalprice(), "totalprice not as expected");
            softAssert.assertEquals(act.getBoolean("depositpaid"), updatedBooking.isDepositpaid(), "depositpaid not as expected");
            softAssert.assertEquals(act.getString("bookingdates.checkin"), updatedBooking.getBookingdates().getCheckin(),"bookingdates.checkin not as expected");
            softAssert.assertEquals(act.getString("bookingdates.checkout"), updatedBooking.getBookingdates().getCheckout(),"bookingdates.checkout not as expected");
            softAssert.assertEquals(act.getString("additionalneeds"), updatedBooking.getAdditionalneeds(),"additionalneeds not as expected");
            softAssert.assertAll();
        }
    }

    //Individual validators
    protected BookingIds createBookings(int i){
        String fName = createFirstName(i);
        String lName = createLastName(i);
        int price = createPrice(i);
        boolean paid= createDepositPaid(i);
        String checkin = createCheckin(i);
        String checkout = createCheckout(i);
        String needs= createNeeds(i);
        Bookings booking = new Bookings(fName,lName,price,paid,checkin,checkout,needs);
        Response response = given(spec).contentType(ContentType.JSON).body(booking)
                .post("/booking");
        response.print();
        BookingIds bookingid = response.as(BookingIds.class);
        Assert.assertEquals(response.getStatusCode(), 200, "StatusCode not as expected");
        Assert.assertEquals(bookingid.getBooking().toString(), booking.toString());
        return bookingid;
    }

    private void validateIdBooking(BookingIds booking) {
        int id = booking.getBookingid();
        Response response = RestAssured.given(spec).get("/booking/" + String.valueOf(id));
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        SoftAssert softAssert = new SoftAssert();
        JsonPath act= response.jsonPath();
        Bookings bookingData = booking.getBooking();
        softAssert.assertEquals(act.getString("firstname"),bookingData.getFirstname(), "firstname not as expected");
        softAssert.assertEquals(act.getString("lastname"),bookingData.getLastname(), "lastname not as expected");
        softAssert.assertEquals(act.getInt("totalprice"),bookingData.getTotalprice(), "totalprice not as expected");
        softAssert.assertEquals(act.getBoolean("depositpaid"),bookingData.isDepositpaid(), "depositpaid not as expected");
        softAssert.assertEquals(act.getString("bookingdates.checkin"),bookingData.getBookingdates().getCheckin(),"bookingdates.checkin not as expected");
        softAssert.assertEquals(act.getString("bookingdates.checkout"),bookingData.getBookingdates().getCheckout(),"bookingdates.checkout not as expected");
        softAssert.assertEquals(act.getString("additionalneeds"),bookingData.getAdditionalneeds(),"additionalneeds not as expected");
        softAssert.assertAll();
    }

    private Bookings updateBookings(int j, BookingIds bookingIds) {
        int bookingId = bookingIds.getBookingid();
        String fName = updateFirstName(j);
        String lName = updateLastName(j);
        int price = updatePrice(j);
        boolean paid= updateDepositPaid(j);
        String checkin = updateCheckin(j);
        String checkout = updateCheckout(j);
        String needs= updateNeeds(j);
        Bookings booking = new Bookings(fName,lName,price,paid,checkin,checkout,needs);
        Response responseUpdate = RestAssured
                .given(spec)
                .auth()
                .preemptive()
                .basic("admin", "password123")
                .contentType(ContentType.JSON)
                .body(booking)
                .put("/booking/" + bookingId);
        responseUpdate.print();

        Bookings bookingid = responseUpdate.as(Bookings.class);
        Assert.assertEquals(responseUpdate.getStatusCode(), 200, "StatusCode not as expected");
        Assert.assertEquals(bookingid.toString(), booking.toString());
        return bookingid;
    }

    //helpers
    protected String createFirstName(int i){
        return "Pranay" + String.valueOf(i);
    }
    protected String createLastName(int i){
        return "Reddy" + String.valueOf(i);
    }
    protected int createPrice(int i){
        return 100 +i;
    }
    protected boolean createDepositPaid(int i){
        return i%2==0;
    }
    protected String createCheckin(int i){
        return todaysDate.plusDays(i).format(formatter);
    }
    protected String createCheckout(int i){
        return todaysDate.plusDays(i+5).format(formatter);
    }
    protected String createNeeds(int i){
        return "Needs-" + String.valueOf(i);
    }

    protected String updateFirstName(int i){
        return "Pranay Kumar" + String.valueOf(i);
    }
    protected String updateLastName(int i){
        return "Reddy Ravula" + String.valueOf(i);
    }
    protected int updatePrice(int i){
        return 200 +i;
    }
    protected boolean updateDepositPaid(int i){
        return i%2!=0;
    }
    protected String updateCheckin(int i){
        return todaysDate.plusDays(i+100).format(formatter);
    }
    protected String updateCheckout(int i){
        return todaysDate.plusDays(i+105).format(formatter);
    }
    protected String updateNeeds(int i){
        return "Updated needs-" + String.valueOf(i);
    }
}
