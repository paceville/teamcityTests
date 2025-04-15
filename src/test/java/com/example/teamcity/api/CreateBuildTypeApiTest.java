package com.example.teamcity.api;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static com.example.teamcity.api.requests.enums.Endpoint.*;

public class CreateBuildTypeApiTest extends BaseApiTest {

    @Test
    @DisplayName("User should not be able to create two build types with the same id")
    @Tags({
            @Tag("regression"),
            @Tag("negative")
    })
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        var user = generate(User.class);

        superUserCheckRequests.getRequest(USERS).create(user);
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(user));

        var project = generate(Project.class);

        project = userCheckRequests.<Project>getRequest(PROJECTS).create(project);

        var buildType1 = generate(Arrays.asList(project), BuildType.class);
        var buildType2 = generate(Arrays.asList(project), BuildType.class, buildType1.getId());
        buildType2.getProject().setLocator(null);
        buildType2.setId(buildType1.getId());

        userCheckRequests.getRequest(BUILD_TYPES).create(buildType1);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(buildType2);
            System.out.println("Request body for buildType2: " + requestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        new UncheckedBase(Specifications.authSpec(user), BUILD_TYPES)
                .create(buildType2)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(buildType1.getId())));
    }


}
