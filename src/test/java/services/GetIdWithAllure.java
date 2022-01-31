package services;

import io.qameta.allure.Allure;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetIdWithAllure {

    @Test
    public void getBookingIds () {
        RequestSpecification httpRequest = given().log().all();
        String url = "https://restful-booker.herokuapp.com/booking";
        Response response = httpRequest.get(url);
        attachment(httpRequest, url, response);
    }

    public String attachment (RequestSpecification httpRequest, String baseUrl, Response response)
    {
        String html = "Url"+ baseUrl + "\n\n"+
                "request header="+ ((RequestSpecificationImpl) httpRequest).getHeaders() + "\n\n"+
                "request body="+ ((RequestSpecificationImpl) httpRequest).getBody() + "\n\n"+
                "response body="+ response.getBody().asString();
        Allure.addAttachment("request detail", html) ;
        return html;
    }
}