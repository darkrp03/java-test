package org.example.lab2;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetTest {
    private static final String baseUrl = "https://petstore.swagger.io/v2";

    private static final String PET = "/pet",
            PET_BY_ID = PET + "/{petId}";

    // Variables with group and student number
    private static final int GROUP_NUMBER = 121;
    private static final int STUDENT_NUMBER = 20;

    // Variables with student name or initials
    private static final String STUDENT_NAME = "A.Monin";

    private long petId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyPostPet() {
        Faker faker = Faker.instance();
        petId = GROUP_NUMBER * 1000 + STUDENT_NUMBER;

        Map<String, ?> body = Map.of(
                "id", petId,
                "name", STUDENT_NAME,
                "category", Map.of("id", GROUP_NUMBER, "name", "Group Category"),
                "photoUrls", new String[]{"https://example.com/photo.jpg"},
                "tags", new Object[]{Map.of("id", STUDENT_NUMBER, "name", "TagName")},
                "status", "available"
        );

        given().body(body)
                .post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("name", equalTo(STUDENT_NAME));
    }

    @Test(dependsOnMethods = "verifyPostPet")
    public void verifyGetPet() {
        given().pathParam("petId", petId)
                .get(PET_BY_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo((int) petId))
                .and()
                .body("name", equalTo(STUDENT_NAME));
    }

    @Test(dependsOnMethods = "verifyGetPet")
    public void verifyPutPet() {
        String updatedName = STUDENT_NAME + "-Updated";

        Map<String, ?> body = Map.of(
                "id", petId,
                "name", updatedName,
                "category", Map.of("id", GROUP_NUMBER, "name", "Updated Category"),
                "photoUrls", new String[]{"https://example.com/updated-photo.jpg"},
                "tags", new Object[]{Map.of("id", STUDENT_NUMBER, "name", "UpdatedTagName")},
                "status", "sold"
        );

        given().body(body)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("name", equalTo(updatedName))
                .and()
                .body("status", equalTo("sold"));
    }

    @Test(dependsOnMethods = "verifyPutPet")
    public void verifyDeletePet() {
        given().pathParam("petId", petId)
                .delete(PET_BY_ID)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
