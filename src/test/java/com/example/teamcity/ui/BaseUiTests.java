package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.BaseTest;
import com.example.teamcity.api.config.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

public class BaseUiTests extends BaseTest {
    static String http = "http://";

    @BeforeAll
    @DisplayName("Set up browser")
    public static void setupUiTest() {
        Configuration.browser = Config.getProperty("browser");
        Configuration.baseUrl = http + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");
        Configuration.browserSize = Config.getProperty("browserSize");
        Configuration.browserCapabilities.setCapability("selenoid:options", Map.of("enableVNC", true, "enableLog", true));
    }

    @AfterEach
    @DisplayName("Close browser")
    public void closeWebDriver() {
        Selenide.closeWebDriver();
    }
}
