package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PartialUpdateBookingTests extends BaseTest  {
    @Test
    public void partialUpdateBookingTest() {
        Response responseCreate = createBooking();
        responseCreate.print();

        int bookingId = responseCreate.jsonPath().getInt("bookingid");

        JSONObject body = new JSONObject();
        body.put("firstname", "Pranay Kumar");

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2024-03-21");
        bookingDates.put("checkout", "2024-03-25");
        body.put("bookingdates", bookingDates);

        Response responseUpdate = RestAssured
                .given(spec)
                .auth()
                .preemptive()
                .basic("admin", "password123")
                .contentType(ContentType.JSON)
                .body(body.toString())
                .patch("/booking/" + bookingId);
        responseUpdate.print();

        Assert.assertEquals(responseUpdate.getStatusCode(), 200, "StatusCode not as expected");
        SoftAssert softAssert = new SoftAssert();
        JsonPath act= responseUpdate.jsonPath();
        softAssert.assertEquals(act.getString("firstname"),
                "Pranay Kumar", "firstname not as expected");
        softAssert.assertEquals(act.getString("lastname"),
                "Reddy", "lastname not as expected");
        softAssert.assertEquals(act.getInt("totalprice"),
                100, "totalprice not as expected");
        softAssert.assertEquals(act.getBoolean("depositpaid"),
                false, "depositpaid not as expected");
        softAssert.assertEquals(act.getString("bookingdates.checkin"),
                "2024-03-21","bookingdates.checkin not as expected");
        softAssert.assertEquals(act.getString("bookingdates.checkout"),
                "2024-03-25","bookingdates.checkout not as expected");
        softAssert.assertEquals(act.getString("additionalneeds"),
                "food","additionalneeds not as expected");
        softAssert.assertAll();
    }
}
