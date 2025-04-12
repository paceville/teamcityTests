package com.example.teamcity.ui.pages;

import java.time.Duration;

public abstract class BasePage {
    protected static final Duration BASE_WAITING = Duration.ofSeconds(30);
    protected static final Duration LONG_WAITING = Duration.ofMinutes(3);
}
