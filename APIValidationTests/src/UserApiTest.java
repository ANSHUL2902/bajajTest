import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;

public class UserApiTest {
	@BeforeAll
    public static void setup() {
      
        RestAssured.baseURI = "https://bfhldevapigw.healthrx.co.in/automation-campus/create/user";
    }

    @Test
    public void testCreateUserSuccess() {
        given()
            .header("roll-number", "1")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"Anshul\", \"lastName\": \"Sarade\", \"phoneNumber\": 1234567890, \"emailId\": \"Anshul@example.com\" }")
        .when()
            .post()
        .then()
            .statusCode(200)
            .body("message", equalTo("User created successfully"));  
    }

    @Test
    public void testCreateUserWithoutRollNumber() {
        given()
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"mayur\", \"lastName\": \"patil\", \"phoneNumber\": 1234567891, \"emailId\": \"mayur@example.com\" }")
        .when()
            .post()
        .then()
            .statusCode(401)
            .body("error", equalTo("Unauthorized"));  
    }

    @Test
    public void testCreateUserWithDuplicatePhoneNumber() {
       
        given()
            .header("roll-number", "10")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"anil\", \"lastName\": \"yadav\", \"phoneNumber\": 1234567892, \"emailId\": \"anil@gmail.com\" }")
        .when()
            .post()
        .then()
            .statusCode(200);  

       
        given()
            .header("roll-number", "10")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"manav\", \"lastName\": \"sateja\", \"phoneNumber\": 1234567892, \"emailId\": \"manav@gmail.com\" }")
        .when()
            .post()
        .then()
            .statusCode(400)
            .body("error", equalTo("Phone number already exists"));  
    }

    @Test
    public void testCreateUserWithDuplicateEmailId() {
       
        given()
            .header("roll-number", "4")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"Charlie\", \"lastName\": \"Brown\", \"phoneNumber\": 1234567893, \"emailId\": \"charlie.brown@example.com\" }")
        .when()
            .post()
        .then()
            .statusCode(200);  

       
        given()
            .header("roll-number", "5")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"David\", \"lastName\": \"Wilson\", \"phoneNumber\": 1234567894, \"emailId\": \"charlie.brown@example.com\" }")
        .when()
            .post()
        .then()
            .statusCode(400)
            .body("error", equalTo("Email ID already exists"));  // Adjust according to your API's error message
    }
}
