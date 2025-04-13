package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.enums.Endpoint;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Tag("refactoring")
public class CreateProjectTest extends BaseUiTests {
    User user;
    TestData testdata = generate();

    //@Test
    @DisplayName("User should be able to create a new project")
    @Tags({
            @Tag("refactoring"),
            @Tag("positive")
    })
    public void userCreatesProjectTest() {
        loginAsUser(user);

        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testdata.getProject().getName(), testdata.getProject().getId());

        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS)
                .read("name:" + testdata.getProject().getName());

        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText(testdata.getProject().getName()));

        softy.assertThat(createdProject).isNotNull();
    }


    //@Test
    @DisplayName("User cannot create a new project with missing optional field")
    @Tags({
            @Tag("refactoring"),
            @Tag("negative")
    })
    public void userCannotCreateProjectWithMissingOptionalFieldTest() {
        step("Login as user");
        loginAsUser(user);
        step("Open 'Create Project Page' (http://localhost:8111/admin/createObjectMenu.html)");
        CreateProjectPage projectPage = CreateProjectPage.open("_Root")
                .createForm(REPO_URL);

        // Устанавливаем данные проекта с пустым необязательным полем
        projectPage.setupProject(testdata.getProject().getName(), "");
       // projectPage.clickProceedButton();

        // Инициализация объекта CreateBuildTypePage
        CreateBuildTypePage createBuildTypePage = CreateBuildTypePage.open("_Root");

        // Проверка ошибки на UI
        step("Check that error message is displayed on Create Build Type Page");
        createBuildTypePage.verifyErrorMessage();

        // Проверка состояния API
        step("Check that project was not created on API Level");
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS)
                .read("name:" + testdata.getProject().getName());
        softy.assertThat(createdProject).isNull();
    }

    //@Test
    @DisplayName("User should be able to create a new project")
    @Tag("refactoring")
    public void userCreatesProjectTest1() {
        loginAsUser(user);

        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testdata.getProject().getName(), testdata.getProject().getId());

        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS)
                .read("name:" + testdata.getProject().getName());

        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText(testdata.getProject().getName()));

        softy.assertThat(createdProject).isNotNull();
    }

}
