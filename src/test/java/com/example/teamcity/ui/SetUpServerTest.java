package com.example.teamcity.ui;

import com.example.teamcity.ui.pages.setup.FirstStartPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SetUpServerTest extends BaseUiTests {

    @Test
    @Tag("setup")
    public void setupTeamCityServerTest() {
        FirstStartPage.open().setUpFirstStep();
    }
}
