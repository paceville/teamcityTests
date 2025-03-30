package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.enums.Endpoint;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UncheckedBase extends Request implements CrudInterface {

    public UncheckedBase(RequestSpecification specification, Endpoint endpoint) {
        super(specification, endpoint);
    }

    @Override
    public Response create(BaseModel model) {
        System.out.println("Base URI: " + RestAssured.baseURI);
        System.out.println("Base Path: " + RestAssured.basePath);

        return RestAssured
                .given()
                .spec(specification)
                .body(model)
                .post(endpoint.getUrl());
    }

    @Override
    public Response read(String locator) {
        return RestAssured
                .given()
                .spec(specification)
                .get(endpoint.getUrl() + "/" + locator);
    }

    @Override
    public Response update(String locator, BaseModel model) {
        return RestAssured
                .given()
                .body(model)
                .spec(specification)
                .put(endpoint.getUrl() + "/" + locator);
    }

    @Override
    public Response delete(String locator) {
        return RestAssured
                .given()
                .spec(specification)
                .delete(endpoint.getUrl() + "/" + locator);
    }
}
