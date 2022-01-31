package services;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BookingTest {

    String token;

    @BeforeClass
    public Object[][] postCreateToken() {
        String post = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        Response response =
                given()
                        .log().all()
                        .contentType(ContentType.JSON)
                        .body(post).
                        when()
                        .post("https://restful-booker.herokuapp.com/auth");
        System.out.println(response.toString());
        System.out.println(response.then().log().all());
        Assert.assertEquals(response.getStatusCode(), 200);
        token = response.jsonPath().getString("token");

        return new Object[0][];

    }

        @Test
        public void postCreateBooking () {
            String postData = "{\n" +
                    "    \"firstname\" : \"Jim\",\n" +
                    "    \"lastname\" : \"Brown\",\n" +
                    "    \"totalprice\" : 111,\n" +
                    "    \"depositpaid\" : true,\n" +
                    "    \"bookingdates\" : {\n" +
                    "        \"checkin\" : \"2018-01-01\",\n" +
                    "        \"checkout\" : \"2019-01-01\"\n" +
                    "    },\n" +
                    "    \"additionalneeds\" : \"Breakfast\"\n" +
                    "}";
            given()
                    .body(postData)
                    .contentType(ContentType.JSON)
                    .log().all().
                    when()
                    .post("https://restful-booker.herokuapp.com/booking").
                    then()
                    .statusCode(200)
                    .log().all();
        }

        @Test
        public void getBookingIds () {
            given()
                    .log().all().
                    when()
                    .get("https://restful-booker.herokuapp.com/booking").
                    then()
                    .statusCode(200)
                    .log().all();
        }

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider ()
    {
        return new Object[][]{
                {1,200}
        };
    }

    @Test (dataProvider = "dataProvider")
        public void getBookingDetail (int id, int status) {

            given()
                    .log().all().
                    when()
                    .get("https://restful-booker.herokuapp.com/booking/" + id).
                    then()
                    .statusCode(status)
                    .body("additionalneeds", equalTo("Breakfast"))
                    .log().all();
        }


        @Test
        public void putUpdateBooking () {
            int bookingId = 5;

                String jsonString = "{\"firstname\" : \"James\",\n" +
                        "    \"lastname\" : \"Brown\",\n" +
                        "    \"totalprice\" : 111,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2018-01-01\",\n" +
                        "        \"checkout\" : \"2019-01-01\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"}";

                given()
                        .log().all().
                        baseUri("https://restful-booker.herokuapp.com/booking/" + bookingId).
                        contentType(ContentType.JSON).
                        header("Content-Type", "Application/json")
                        .cookie("token",token)
                        .body(jsonString).
                        when()
                        .put().
                        then()
                        .assertThat().statusCode(200)
                        .log().all();

            }

            @AfterClass
            public void deleteBooking (){
            int bookingId = 5;

                given()
                        .log().all()
                        .cookie("token",token)
                        .baseUri("https://restful-booker.herokuapp.com/booking/" + bookingId)
                        .header("Content-Type", "Application/json").
                        when()
                        .delete().
                        then()
                        .statusCode(201).log().all();
            }
}
