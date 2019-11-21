package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.TodosController;
import com.edu.entity.Todo;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

public class TodosTest {

    private ControllerSpecification todos = new TodosController();

    @Test
    @Story("8")
    @Description("Verify HTTP status code and completion status of the 10th task.")
    public void test_8() {
      ValidatableResponse response = given()
              .spec(todos.getRequestSpecification())
              .pathParam("taskId", "10")
              .when()
              .get()
              .then()
              .spec(todos.getResponseSpecification())
              .assertThat()
              .statusCode(HttpStatus.SC_OK);
      Todo task = response.extract().as(Todo.class);
      assertTrue(task.getCompleted());
    }
}
