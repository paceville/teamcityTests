package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.enums.Endpoint;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static com.example.teamcity.api.requests.enums.Endpoint.*;
import static io.qameta.allure.Allure.step;

public class CreateBuildTypeTest extends BaseUiTests {
    User user;
    TestData testdata = generate();

    @BeforeEach
    public void createUser() {
        user = generate(User.class); // Сначала генерируем объект User
        step("Create user", () -> superUserCheckRequests.getRequest(USERS).create(user)); // Затем создаём его через API
    }

    @Test
    @DisplayName("User should be able to create a new build configuration")
    @Tag("positive")
    public void userCreatesBuildConfigurationTest() {
        var project = testdata.getProject();
        var buildType = testdata.getBuildType();

        step("Create a project via API");
        superUserCheckRequests.getRequest(Endpoint.PROJECTS).create(project);

        step("Login as user");
        loginAsUser(user);

        step("Open 'Create Build Configuration Page'");
        CreateBuildTypePage.open(project.getId())
                .createForm(REPO_URL)
                .setupBuildType(buildType.getName());

        step("Get created build type via API");
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES)
                .read("name:" + buildType.getName());

        step("Check build type exists via API");
        softy.assertThat(createdBuildType).isNotNull();

        step("Check build type is visible in UI");
        ProjectPage.open(project.getId())
                .title.shouldHave(Condition.exactText(project.getName()));

        afterTest();
    }

    @Test
    @DisplayName("User cannot create a new build configuration with missing required field")
    @Tag("negative")
    public void userCannotCreateBuildConfigurationWithMissingRequiredFieldTest() {
        var project = testdata.getProject();
        var buildType = testdata.getBuildType();

        step("Create a project via API");
        superUserCheckRequests.getRequest(Endpoint.PROJECTS).create(project);

        step("Login as user");
        loginAsUser(user);

        step("Open 'Create Build Configuration Page'");
        CreateBuildTypePage page = CreateBuildTypePage.open(project.getId())
                .createForm(REPO_URL);

        page.setupBuildTypeNegative("");

        step("Check that error message is displayed on Create Build Type Page");
        page.verifyErrorMessage();

        step("Check that build type was not created on API Level");
        UncheckedBase requester = new UncheckedBase(Specifications.authSpec(user), Endpoint.BUILD_TYPES);
        var response = requester.read("name:" + buildType.getName());

        step("Check API response status code is 404");
        softy.assertThat(response.statusCode()).isEqualTo(404);

        step("Check that build type is null");
        softy.assertThat(response.asString()).isEmpty(); // Проверяем, что ответ пустой
    }

}
