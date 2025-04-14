package com.example.teamcity.ui;

import com.example.teamcity.ui.pages.setup.FirstStartPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SetUpServerTest extends BaseUiTests {

    @Test
    @Tag("setup")
    public void setupTeamCityServerTest() {

        var firstStartPage = FirstStartPage.open();
        if (firstStartPage == null) {
            // setup не нужен — TC уже инициализирован, можно просто выйти из теста
            return;
        }

        // тут твой код для setup
        FirstStartPage.setUpFirstStep();
    }
}
