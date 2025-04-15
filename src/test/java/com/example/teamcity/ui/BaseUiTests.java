package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.BaseTest;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.example.teamcity.ui.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static com.example.teamcity.api.requests.enums.Endpoint.USERS;
import static io.qameta.allure.Allure.step;

public class BaseUiTests extends BaseTest {
    static String http = "http://";
    final static String REPO_URL = "https://github.com/ALexPshe/spring-core-for-qa";

    @BeforeAll
    @DisplayName("Set up browser")
    public static void setupUiTest() {
        Configuration.browser = Config.getProperty("browser");
        Configuration.baseUrl = http + Config.getProperty("host") + ":" + Config.getProperty("port");
        Configuration.remote = Config.getProperty("remote");
        Configuration.browserSize = Config.getProperty("browserSize");
        Configuration.browserCapabilities.setCapability("selenoid:options", Map.of("enableVNC", true, "enableLog", true));
    }

    @AfterEach
    @DisplayName("Close browser")
    public void closeWebDriver() {
        Selenide.closeWebDriver();
    }

    protected void loginAsUser(User user) {
        user = createUser();
        LoginPage.open().login(user);
    }

    private User createUser() {
        var user = generate(User.class);
        step("Create user", () -> superUserCheckRequests.getRequest(USERS).create(user));
        return user;
    }
}
