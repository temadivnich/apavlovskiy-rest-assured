package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.TodosController;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.is;

public class TodosTest {

    private final ControllerSpecification todos = new TodosController();

    @Test
    @Story("8")
    @Description("Verify HTTP status code and completion status of the 10th task.")
    public void test_8() {
        todos.get("10")
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("completed", is(true));
    }
}
