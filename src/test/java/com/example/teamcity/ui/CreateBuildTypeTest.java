package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.enums.Endpoint;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
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
    @DisplayName("User should be able to create a new build configuration via UI clicks")
    @Tags({
            @Tag("regression"),
            @Tag("positive")
    })
    public void userCreatesBuildConfigurationViaUiTest() {
        var project = testdata.getProject();
        var buildType = testdata.getBuildType();

        step("Create a project via API", () -> {
            superUserCheckRequests.getRequest(Endpoint.PROJECTS).create(project);
        });

        step("Login as user", () -> {
            loginAsUser(user);
        });

        step("Open project page via UI", () -> {
           // ProjectPage.open(project.getId())
             //       .title.shouldHave(Condition.exactText(project.getName()));
            String projectName = project.getName();

            Selenide.$(String.format("span[title='%s'] span.MiddleEllipsis__searchable--uZ", projectName))
                    .shouldBe(Condition.visible, Duration.ofSeconds(10))
                    .click();

        });

        step("Click on 'New project...' and select 'Create build configuration'", () -> {
            $("button[title='New project...']").click(); // или $("button").find(Condition.text("New project...")).click();
            $(byText("Create build configuration")).click();
        });

        step("Fill out form with repository URL", () -> {
            new CreateBuildTypePage().createForm(REPO_URL);
        });

        step("Enter build configuration name and finish setup", () -> {
            new CreateBuildTypePage().setupBuildType(buildType.getName());
        });

        step("Verify build configuration exists via API", () -> {
            var createdBuildType = superUserCheckRequests.<BuildType>getRequest(BUILD_TYPES)
                    .read("name:" + buildType.getName());
            softy.assertThat(createdBuildType).isNotNull();
        });

        step("Verify build configuration is visible on project page", () -> {
            ProjectPage.open(project.getId())
                    .title.shouldHave(Condition.exactText(project.getName()));
        });

        afterTest();
    }

    @Test
    @DisplayName("User cannot create a new build configuration with missing required field")
    @Tags({
            @Tag("regression"),
            @Tag("negative")
    })
    public void userCannotCreateBuildConfigurationWithMissingRequiredFieldTest() {
        var project = testdata.getProject();
        var buildType = testdata.getBuildType();

        step("Create a project via API");
        superUserCheckRequests.getRequest(Endpoint.PROJECTS).create(project);

        step("Login as user");
        loginAsUser(user);

        step("Open 'Create Build Configuration Page'");
        CreateBuildTypePage page = CreateBuildTypePage.openWithRetry(project.getId(), 5, Duration.ofSeconds(3))
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
