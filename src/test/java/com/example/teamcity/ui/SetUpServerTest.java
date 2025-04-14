package com.example.teamcity.ui;

import com.example.teamcity.ui.pages.setup.FirstStartPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SetUpServerTest extends BaseUiTests {

    @Test
    @DisplayName("Set up TeamCity server")
    public void setupTeamCityServerTest() {
        try {
            FirstStartPage.open().setUpFirstStep();
        } catch (IllegalStateException e) {
            System.out.println("Skipping setup: " + e.getMessage());
        }
    }
}
