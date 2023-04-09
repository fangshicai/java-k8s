package com.fsc.model.response.harbor.tags;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Tags {
    @JsonProperty("artifact_id")
    @SerializedName("artifact_id")
    private Integer artifactId;
    @SerializedName("id")
    @JsonProperty("id")
    private Integer id;
    @SerializedName("immutable")
    @JsonProperty("immutable")
    private Boolean immutable;
    @SerializedName("name")
    @JsonProperty("name")
    private String name;
    @SerializedName("pullTime")
    @JsonProperty("pull_time")
    private String pullTime;
    @SerializedName("pushTime")
    @JsonProperty("push_time")
    private String pushTime;
    @SerializedName("repositoryId")
    @JsonProperty("repository_id")
    private Integer repositoryId;
    @SerializedName("signed")
    @JsonProperty("signed")
    private Boolean signed;

}

