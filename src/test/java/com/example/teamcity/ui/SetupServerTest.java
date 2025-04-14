package com.example.teamcity.ui;

import com.example.teamcity.ui.setup.FirstStartPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Fail.fail;

public class SetupServerTest extends BaseUiTests {
    @Test
    @Tag("Setup")
    public void setupTeamCityServerTest(){
        FirstStartPage.open().setupFirstStart();
    }

    @Test
    @Tag("Setup")
    void willFailIfIncludedAccidentallysSecond() {
        fail("This test should be excluded too");
    }

}