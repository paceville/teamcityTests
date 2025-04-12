package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.ProjectPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class CreateBuildTypePage extends CreateBasePage {
    private static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";
    private static final String NAME_MUST_NOT_BE_EMPTY = "Name must not be empty";

    private SelenideElement successMessage = $(".successMessage");
    private SelenideElement scanningVcsRepositories = $(byText("Scanning VCS repositories"));
    private SelenideElement divUnprocessedObjectsCreated = $("div[id$='objectsCreated']");
    private SelenideElement errorMessage = $("#error_url");
    private SelenideElement errorBuildTypeName = $("#error_buildTypeName");

    public static CreateBuildTypePage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_TYPE_SHOW_MODE), CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createForm(String url) {
        baseCreateForm(url);
        return this;
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

}

