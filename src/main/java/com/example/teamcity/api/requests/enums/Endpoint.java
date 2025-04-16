package com.example.teamcity.api.requests.enums;

import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import lombok.Getter;

@Getter
public enum Endpoint {
    PROJECTS("/app/rest/projects", Project.class),
    USERS("/app/rest/users", User.class);

    private final String url;
    private final Class<? extends BaseModel> modelClass;

    Endpoint(String url, Class<? extends BaseModel> modelClass) {
        this.url = url;
        this.modelClass = modelClass;
    }

    public Class<? extends BaseModel> getModelClass() {
        return modelClass;
    }

    public String getUrl() {
        return url;
    }
}
