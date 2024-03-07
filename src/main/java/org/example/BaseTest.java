package org.example;

import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected static RequestSpecification spec = new RequestSpecBuilder().
            setBaseUri("https://restful-booker.herokuapp.com").
            build();;
    protected static LocalDate todaysDate = LocalDate.now();
    protected static DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeMethod
    public void setUp() {
        spec = new RequestSpecBuilder().
                setBaseUri("https://restful-booker.herokuapp.com").
                build();
    }
    protected Response createBooking() {
        JSONObject body = new JSONObject();
        body.put("firstname", "Pranay");
        body.put("lastname", "Reddy");
        body.put("totalprice", 100);
        body.put("depositpaid", false);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2024-03-20");
        bookingdates.put("checkout", "2024-03-24");
        body.put("bookingdates", bookingdates);
        body.put("additionalneeds", "food");
        Response response = RestAssured.given(spec).contentType(ContentType.JSON).body(body.toString())
                .post("/booking");
        return response;
    }
}