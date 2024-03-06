package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;


public class GetBookingIdsTest extends BaseTest  {
    @Test
    public void getBookingIdsWithoutFilterTest() {
        Response response = RestAssured.given(spec).get("/booking");
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "StatusCode not as expected");

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List of bookingIds not as expected");
    }
    @Test
    public void getBookingIdsWithFilterTest() {
        spec.queryParam("firstname", "Jim");
        spec.queryParam("lastname", "Smith");

        Response response = RestAssured.given(spec).get("/booking");
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "StatusCode not as expected");

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List of bookingIds not as expected");
    }
}
