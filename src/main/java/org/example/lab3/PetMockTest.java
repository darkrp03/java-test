package org.example.lab3;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PetMockTest {

    private static final String BASE_URL = "https://22776a9d-abfd-4523-b6bb-8df2e7a3cf93.mock.pstmn.io/v2/pet"; // Replace with your mock server URL
    private static final String PET_BODY = "{"
            + "\"id\": 0,"
            + "\"category\": {\"id\": 0, \"name\": \"string\"},"
            + "\"name\": \"doggie\","
            + "\"photoUrls\": [\"string\"],"
            + "\"tags\": [{\"id\": 0, \"name\": \"string\"}],"
            + "\"status\": \"available\""
            + "}";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void testAddPetSuccess() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(PET_BODY)
                .when()
                .post("")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 200, "Expected 200 OK");
    }

    @Test
    public void testAddPetBadRequest() {
        String invalidBody = "{}"; // Empty request body

        Response response = given()
                .contentType(ContentType.JSON)
                .body(invalidBody)
                .when()
                .post("")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 400, "Expected 400 Bad Request");
    }

    @Test
    public void testUpdatePetSuccess() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(PET_BODY)
                .when()
                .put("")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 200, "Expected 200 OK");
    }

    @Test
    public void testUpdatePetNotFound() {
        String nonExistingPet = "{"
                + "\"id\": 346546," // Updated ID as requested
                + "\"category\": {\"id\": 0, \"name\": \"string\"},"
                + "\"name\": \"doggie\","
                + "\"photoUrls\": [\"string\"],"
                + "\"status\": \"available\""
                + "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(nonExistingPet)
                .when()
                .put("")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 404, "Expected 404 Not Found");
    }

    @Test
    public void testDeletePetSuccess() {
        Response response = given()
                .when()
                .delete("/0")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 200, "Expected 200 OK");
    }

    @Test
    public void testDeletePetNotFound() {
        Response response = given()
                .when()
                .delete("/346546") // Non-existing pet
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 404, "Expected 404 Not Found");
    }

    @Test
    public void testGetPetByIdValid() {
        Response response = given()
                .when()
                .get("/0")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 200, "Expected 200 OK");
    }

    // New test for Get Pet by ID (Invalid ID)
    @Test
    public void testGetPetByIdNotFound() {
        Response response = given()
                .when()
                .get("/346546") // Non-existing pet ID
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 404, "Expected 404 Not Found");
    }
}
