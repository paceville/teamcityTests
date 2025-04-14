package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.ProjectPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CreateBuildTypePage extends CreateBasePage {
    private static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";
    private static final String NAME_MUST_NOT_BE_EMPTY = "Name must not be empty";

    private SelenideElement successMessage = $(".successMessage");
    private SelenideElement scanningVcsRepositories = $(byText("Scanning VCS repositories"));
    private SelenideElement divUnprocessedObjectsCreated = $("div[id$='objectsCreated']");
    private SelenideElement errorMessage = $("#error_url");
    private SelenideElement errorBuildTypeName = $("#error_buildTypeName");
    private SelenideElement jumpButton = $("button[aria-label^='Jump']");
    private SelenideElement newBuildConfiguration = $$(("a.ring-link-link")).findBy(Condition.text("New build configuration"));


    public static CreateBuildTypePage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_TYPE_SHOW_MODE), CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public static CreateBuildTypePage openWithRetry(String projectId, int retries, Duration waitBetween) {
        for (int i = 0; i < retries; i++) {
            CreateBuildTypePage page = Selenide.open(CREATE_URL.formatted(projectId, BUILD_TYPE_SHOW_MODE), CreateBuildTypePage.class);
            try {
                $("#url").shouldBe(Condition.visible, Duration.ofSeconds(5));
                return page;
            } catch (Throwable e) {
                System.out.println("Retry " + (i + 1) + ": Page not ready yet");
                Selenide.sleep(waitBetween.toMillis());
            }
        }
        throw new IllegalStateException("Create Build page didn't load after retries");
    }

    public ProjectPage setupBuildType(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        proceedButton.click();
        waitForVcsScanToFinish();
        verifyBuildTypeCreated(buildTypeName);
        System.out.println("BuildTypeID");
        return page(ProjectPage.class);
    }

    public CreateBuildTypePage waitForVcsScanToFinish() {
        scanningVcsRepositories
                .shouldBe(disappear, Duration.ofSeconds(30));
        return this;
    }

    public CreateBuildTypePage verifyBuildTypeCreated(String buildTypeName) {
        divUnprocessedObjectsCreated
                .shouldBe(visible, Duration.ofSeconds(30)); // можно больше, если долго сканирует
        return this;
    }

    public ProjectPage setupBuildTypeNegative(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        proceedButton.click();
        verifyErrorMessage();
        return page(ProjectPage.class);
    }

    public void verifyErrorMessage() {
        errorBuildTypeName.shouldBe(Condition.visible);
        errorBuildTypeName.shouldHave(Condition.text(NAME_MUST_NOT_BE_EMPTY));
    }

    public void openProjectViaUi(String projectName) {
        Selenide.$(String.format("span[title='%s'] span.MiddleEllipsis__searchable--uZ", projectName))
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .click();
    }

    public void openNewBuildConfiguration() {
        jumpButton.shouldBe(Condition.visible).click();
        newBuildConfiguration.shouldBe(Condition.visible).click();
    }

}

