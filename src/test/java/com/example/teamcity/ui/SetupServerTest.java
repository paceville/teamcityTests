package com.example.teamcity.ui;

import com.example.teamcity.ui.setup.FirstStartPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SetupServerTest extends BaseUiTests {

    @Tag("Setup")
    @Test
    public void setupTeamCityServerTest(){
        FirstStartPage.open().setupFirstStart();
    }
}