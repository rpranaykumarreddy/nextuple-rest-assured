package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DeleteBookingTests extends BaseTest {

    @Test
    public void deleteBookingTest() {
        Response responseCreate = createBooking();
        responseCreate.print();

        int bookingId = responseCreate.jsonPath().getInt("bookingid");

        Response responseDelete = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
                .delete("/booking/" + bookingId);
        responseDelete.print();

        assertEquals(responseDelete.getStatusCode(), 201, "StatusCode not as expected");

        Response responseGet = RestAssured.get("https://restful-booker.herokuapp.com/booking/" + bookingId);
        responseGet.print();

        assertEquals(responseGet.getBody().asString(), "Not Found", "Body not as expected");

    }
}
