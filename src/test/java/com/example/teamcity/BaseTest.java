package com.example.teamcity;

import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected SoftAssertions softy;

    protected CheckedRequests superUserCheckRequests = new CheckedRequests(Specifications.superUserAuthSpec());

    @BeforeEach
    public void beforeTest() {
        softy = new SoftAssertions();
    }

    public void afterTest() {
        softy.assertAll();
    }
}
