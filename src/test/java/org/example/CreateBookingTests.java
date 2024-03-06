package org.example;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.*;

public class CreateBookingTests extends BaseTest {

    @Test
    public void createBookingTest() {
        Response response = createBooking();
        response.print();
        Assert.assertEquals(response.getStatusCode(), 200, "StatusCode not as expected");
        SoftAssert softAssert = new SoftAssert();
        JsonPath act= response.jsonPath();
        softAssert.assertEquals(act.getString("booking.firstname"),
                "Pranay", "firstname not as expected");
        softAssert.assertEquals(act.getString("booking.lastname"),
                "Reddy", "lastname not as expected");
        softAssert.assertEquals(act.getInt("booking.totalprice"),
                100, "totalprice not as expected");
        softAssert.assertEquals(act.getBoolean("booking.depositpaid"),
                false, "depositpaid not as expected");
        softAssert.assertEquals(act.getString("booking.bookingdates.checkin"),
                "2024-03-20","bookingdates.checkin not as expected");
        softAssert.assertEquals(act.getString("booking.bookingdates.checkout"),
                "2024-03-24","bookingdates.checkout not as expected");
        softAssert.assertEquals(act.getString("booking.additionalneeds"),
                "food","additionalneeds not as expected");

        softAssert.assertAll();
    }

    @Test
    public void createBookingWithPOJOTest() {
        Bookings booking = new Bookings("Pranay", "Reddy", 100, false, "2024-03-20","2024-03-24", "food");

        Response response = given(spec).contentType(ContentType.JSON).body(booking)
                .post("/booking");
        response.print();
        BookingIds bookingid = response.as(BookingIds.class);
        Assert.assertEquals(response.getStatusCode(), 200, "StatusCode not as expected");

        System.out.println("Request booking : " + booking);
        System.out.println("Response booking: " + bookingid.getBooking().toString());
        Assert.assertEquals(bookingid.getBooking().toString(), booking.toString());
    }
}
