package org.example;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.*;

public class GetBookingTests extends BaseTest {
    @Test
    public void getBookingIdsWithoutFilterTests(){

        Response response = RestAssured.given(spec).get("/booking/5");
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        SoftAssert softAssert = new SoftAssert();
        JsonPath act= response.jsonPath();
        softAssert.assertEquals(act.getString("firstname"),
                "Mark", "firstname not as expected");
        softAssert.assertEquals(act.getString("lastname"),
                "Jackson", "lastname not as expected");
        softAssert.assertEquals(act.getInt("totalprice"),
                259, "totalprice not as expected");
        softAssert.assertEquals(act.getBoolean("depositpaid"),
                true, "depositpaid not as expected");
        softAssert.assertEquals(act.getString("bookingdates.checkin"),
                "2017-05-28","bookingdates.checkin not as expected");
        softAssert.assertEquals(act.getString("bookingdates.checkout"),
                "2019-04-27","bookingdates.checkout not as expected");
        softAssert.assertNull(act.getString("additionalneeds"),"additionalneeds not as expected");
//        softAssert.assertAll();
    }
}
