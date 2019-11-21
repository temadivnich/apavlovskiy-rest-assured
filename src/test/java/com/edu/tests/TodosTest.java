package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.TodosController;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TodosTest {

    private ControllerSpecification todos = new TodosController();

    @Test
    @Story("8")
    @Description("Verify HTTP status code and completion status of the 10th task.")
    public void test_8() {
        given()
                .spec(todos.getRequestSpecification())
                .pathParam("albumId", "3")
                .when()
                .get()
                .then()
                .spec(todos.getResponseSpecification());

    }
}
