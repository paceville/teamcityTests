package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.enums.Endpoint;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Tag("regression")
public class CreateProjectTest extends BaseUiTests {
    User user;
    private final static String REPO_URL = "https://github.com/ALexPshe/spring-core-for-qa";

    @Test
    @DisplayName("User should be able to create a new project")
    @Tag("positive")
    public void userCreatesProjectTest() {
        //подготовка окружения
        step("Login as user");
        loginAsUser(user);

        //взаимодействие с UI
        step("Open 'Create Project Page' (http://localhost:8111/admin/createObjectMenu.html)");

        step("Send all project parameters (repository URL)");
        step("Click 'Proceed'");
        step("Fix Project Name and Build Type values");
        step("Click 'Proceed'");
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject("projectId123", "projectName123");


        //проверка состояния API
        // проверяем корректность отправки данных с UI на API
        step("Check that all entities (project, build type) was successful created with correct data on API Level");
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + "projectName123");
        softy.assertThat(createdProject).isNotNull();
        System.out.println("Project created");

        //проверка состояния UI
        // проверяем корректность считывания данных и отображение данных на UI
        step("Check that project is visible on Projects Page (http://localhost: 8111/favorite/projects) ");
        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText("projectName123"));
    }

    @Test
    @DisplayName("User should not be able to create project with empty name")
    @Tag("negative")
    public void userCreatesProjectWithEmptyName() {
        // подготовка окружения
        step("Login as user");
        step("Send get request to receive all extended projects");

        // взаимодействие с UI
        step("Open 'Create Project Page' (http://localhost:8111/admin/create0bjectMenu.html)");
        step("Send all project parameters (repository URL)");
        step("Click 'Proceed'");
        step("Set Project Name value is empty");
        step("Send get request to check that count of all extended projects didn't change");

        step("Check that error appears 'Project name must not be empty'");
    }
}
