package com.example.teamcity.api;

import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SuiteDisplayName;

import java.util.concurrent.atomic.AtomicReference;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static com.example.teamcity.api.requests.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.requests.enums.Endpoint.USERS;
import static io.qameta.allure.Allure.step;

@SuiteDisplayName("newProjectCreation")
public class TeamcityCreateProjectTest extends BaseApiTest {

    @Test
    @DisplayName("User should be able to create a new project")
    @Tags({
            @Tag("regression"),
            @Tag("positive")
    })
    public void userCreatesProjectTest() {
        var testdata = generate();


       // var user = createUser();
       // var project = generate(Project.class);
       // var projectId = createProject(user, project);

        var user = testdata.getUser();
        var project = testdata.getProject();
        var projectId = project.getId();

        createUser(user);

        System.out.println("User: " + user);
        System.out.println("Project: " + project);
        System.out.println("ProjectID: " + projectId);

        step("check project was created successfully with correct data", () -> {
            var requester = new CheckedBase<Project>(Specifications.authSpec(user), PROJECTS);
            System.out.println("Fetching project with ID: " + projectId);
            System.out.println("Using credentials: " + user.getUsername() + " / " + user.getPassword());
            var createdProject = requester.read(projectId);
            System.out.println("Created project: " + createdProject);


            softy.assertThat(project.getName()).as("Project name is correct").isEqualTo(createdProject.getName());
            softy.assertThat(projectId).as("Project id is correct").isEqualTo(createdProject.getId());
        });
    }

    /**

    @Test
    @DisplayName("User should not be able to create a project with empty parent project")
    @Tags({
            @Tag("regression"),
            @Tag("positive")
    })
    public void userCreatesProjectWithEmptyLocatorTest() {
        var user = createUser();

        step("create a project with empty locator by user", () -> {
            var project = generate(Project.class);
            project.setLocator("");
            new UncheckedBase(Specifications.authSpec(user), PROJECTS)
                    .create(project)
                    .then().assertThat().statusCode(HttpStatus.SC_OK);
        });
    }

    @Test
    @DisplayName("User should not be able to create two projects with the same id")
    @Tags({
            @Tag("regression"),
            @Tag("negative")
    })
    public void userCreatesTwoProjectsWithTheSameIdTest() {
        var user = createUser();
        var firstProject = generate(Project.class);
        createProject(user, firstProject);

        var secondProject = generate(Project.class);
        secondProject.setId(firstProject.getId());
        new UncheckedBase(Specifications.authSpec(user), PROJECTS)
                .create(secondProject)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID \"%s\" is already used".formatted(firstProject.getId())));
    }

    @Test
    @DisplayName("User should not be able to create two projects with the same name")
    @Tags({
            @Tag("regression"),
            @Tag("negative")
    })
    public void userCreatesTwoProjectsWithTheSameNameTest() {
        var user = createUser();
        var firstProject = generate(Project.class);
        createProject(user, firstProject);

        step("create a second project by user", () -> {
            var secondProject = generate(Project.class, firstProject.getName());
            new UncheckedBase(Specifications.authSpec(user), PROJECTS)
                    .create(secondProject)
                    .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(Matchers.containsString("Project with this name already exists: %s".formatted(secondProject.getName())));
        });
    }

    @Test
    @DisplayName("User should not be able to create a project with empty name")
    @Tags({
            @Tag("regression"),
            @Tag("negative")
    })
    public void userCreatesTwoProjectsWithoutNameTest() {
        var user = createUser();

        step("create a project with empty name by user", () -> {
            var project = generate(Project.class, (String) null);
            new UncheckedBase(Specifications.authSpec(user), PROJECTS)
                    .create(project)
                    .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(Matchers.containsString("Project name cannot be empty."));
        });
    }

    @Test
    @DisplayName("User should not be able to create a project with empty ID")
    @Tags({
            @Tag("regression"),
            @Tag("negative")
    })
    public void userCreatesProjectWithEmptyIdTest() {
        var user = createUser();

        step("create a project with empty ID by user", () -> {
            var project = generate(Project.class);
           project.setId("");
            new UncheckedBase(Specifications.authSpec(user), PROJECTS)
                    .create(project)
                    .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body(Matchers.containsString("Project ID must not be empty."));
        });
    }
            */

    private void createUser(User user) {
        //var user = generate(User.class);
        step("Create user", () -> superUserCheckRequests.getRequest(USERS).create(user));
       // return user;
    }

    private String createProject(User user, Project project) {
        var requester = new CheckedBase<Project>(Specifications.authSpec(user), PROJECTS);
        AtomicReference<String> projectId = new AtomicReference<>("");
        step("Create a new project by user", () -> projectId.set(requester.create(project).getId()));
        return projectId.get();
    }



    /** Other checks
     * positive tests:
     * create a new project with right role but not admin
     * create a new project with the minimum required fields
     * create a project with the maximum number of fields
     * create a new project with a long name
     * create a new project with Unicode characters in the name
     * create a new project with various character types in the name (verify that a project can be created with a name containing special characters (_, -, ., @))
     * create a new project with the maximum number of fields
     * create a new project with a specified parent project
     * ------
     * negative tests
     * create a new project with wrong role
     * create a new project with an invalid id (verify that a project cannot be created with an id containing spaces or special characters (!@#$%^&*))
     * create a new project with a name consisting only of spaces
     * create a new project without authentication
     * create a new project with a duplicate locator
     * create a new project with an id exceeding the allowed length
     * create a new project with a non-existent parent project
     */
}
