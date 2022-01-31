package pingServices;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Test
public class HealthCheck {
    public void healthCheck ()
    {
        given()
                .log().all().
                when()
                .get("https://restful-booker.herokuapp.com/ping").
                then()
                .statusCode(201).log().all();
    }
}