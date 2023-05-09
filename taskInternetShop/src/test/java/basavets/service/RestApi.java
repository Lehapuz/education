package basavets.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;


public class RestApi {

    @Test
    public void getUrl() {
        given()
                .baseUri("http://localhost:8080/")
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    public void registration() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Oleg");
        requestBody.put("email", "oleg@mail.ru");
        requestBody.put("password", "888888");

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post("http://localhost:8080/");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("The status code received: " + statusCode);
    }


    @Test
    public void login() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "Vasya@mail.ru");
        requestBody.put("password", "111111");

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post("http://localhost:8080/login");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("The status code received: " + statusCode);
    }

    @Test
    public void logout() {
        RestAssured.given().auth().digest("Vasya@mail.ru", "111111")
                .when()
                .get("http://localhost:8080/logout").then()
                .statusCode(200);
    }


    @Test
    public void orderProduct() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", 5);

        RequestSpecification request = RestAssured.given()
                .auth()
                .preemptive()
                .basic("Vasya@mail.ru", "111111");

        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post("http://localhost:8080/order/3");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("The status code received: " + statusCode);
    }

    @Test
    public void setProduct() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Телевизор Horizont");
        requestBody.put("amount", 25);

        RequestSpecification request = RestAssured.given()
                .auth()
                .preemptive()
                .basic("lesha@mail.ru", "123456789");

        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post("http://localhost:8080/set");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("The status code received: " + statusCode);
    }

    @Test
    public void changeStatus() {

        RestAssured.given().auth().digest("lesha@mail.ru", "123456789")
                .when()
                .get("http://localhost:8080/status/4").then()
                .statusCode(200);
    }
}
