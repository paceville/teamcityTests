package com.example.teamcity.api.spec;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.github.viclovsky.swagger.coverage.FileSystemOutputWriter;
import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.nio.file.Paths;
import java.util.Base64;

import static com.github.viclovsky.swagger.coverage.SwaggerCoverageConstants.OUTPUT_DIRECTORY;

public class Specifications {
    private static Specifications spec;
    private static RequestSpecBuilder reqSpecBuilder() {
        var requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.addFilter(new RequestLoggingFilter());
        requestSpecBuilder.addFilter(new ResponseLoggingFilter());
        requestSpecBuilder.addFilter(new SwaggerCoverageRestAssured(
                new FileSystemOutputWriter(
                        Paths.get("target/" + OUTPUT_DIRECTORY)
                )
        ));
        requestSpecBuilder.addFilter(new AllureRestAssured());
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.setAccept(ContentType.JSON);
        return requestSpecBuilder;
    }

    public static RequestSpecification superUserAuthSpec() {
        var requestSpecBuilder = reqSpecBuilder();
        String superUserToken = Config.getProperty("superUserToken");
        String credentials = ":" + superUserToken;
        String encodedAuth = Base64.getEncoder().encodeToString(credentials.getBytes());
        String authHeader = "Basic " + encodedAuth;
        requestSpecBuilder.addHeader("Authorization", authHeader);
        System.out.println("Authorization Header: " + authHeader);
        requestSpecBuilder.setBaseUri("http://%s:%s".formatted(Config.getProperty("host"), Config.getProperty("port")));

        return requestSpecBuilder.build();
    }

    public static RequestSpecification unauthSpec() {
        var requestSpecBuilder = reqSpecBuilder();
        return requestSpecBuilder.build();
    }

    public static RequestSpecification authSpec(User user) {
        var requestSpecBuilder = reqSpecBuilder();
        requestSpecBuilder.setBaseUri("http://%s:%s@%s:%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host"), Config.getProperty("port")));
        return requestSpecBuilder.build();
    }
}
