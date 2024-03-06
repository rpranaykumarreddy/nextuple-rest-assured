package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UpdateBookingTests extends BaseTest {

    @Test
    public void updateBookingTest() {
        Response responseCreate = createBooking();
        responseCreate.print();

        int bookingId = responseCreate.jsonPath().getInt("bookingid");

        JSONObject body = new JSONObject();
        body.put("firstname", "Pranay Kumar");
        body.put("lastname", "Reddy Ravula");
        body.put("totalprice", 110);
        body.put("depositpaid", true);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2024-03-21");
        bookingdates.put("checkout", "2024-03-25");
        body.put("bookingdates", bookingdates);
        body.put("additionalneeds", "Food & Car");

        Response responseUpdate = RestAssured
                .given(spec)
                .auth()
                .preemptive()
                .basic("admin", "password123")
                .contentType(ContentType.JSON)
                .body(body.toString())
                .put("/booking/" + bookingId);
        responseUpdate.print();

        Assert.assertEquals(responseUpdate.getStatusCode(), 200, "StatusCode not as expected");
        SoftAssert softAssert = new SoftAssert();
        JsonPath act= responseUpdate.jsonPath();
        softAssert.assertEquals(act.getString("firstname"),
                "Pranay Kumar", "firstname not as expected");
        softAssert.assertEquals(act.getString("lastname"),
                "Reddy Ravula", "lastname not as expected");
        int val;
        val = act.getInt("totalprice");
        softAssert.assertEquals(act.getInt("totalprice"),
                110, "totalprice not as expected");
        softAssert.assertEquals(act.getBoolean("depositpaid"),
                true, "depositpaid not as expected");
        softAssert.assertEquals(act.getString("bookingdates.checkin"),
                "2024-03-21","bookingdates.checkin not as expected");
        softAssert.assertEquals(act.getString("bookingdates.checkout"),
                "2024-03-25","bookingdates.checkout not as expected");
        softAssert.assertEquals(act.getString("additionalneeds"),
                "Food & Car","additionalneeds not as expected");
        softAssert.assertAll();
    }
}