package com.fsc.model.response.harbor.projects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ProjectItem {

    @SerializedName("creation_time")
    @JsonProperty("creation_time")
    private String creationTime;

    @SerializedName("update_time")
    @JsonProperty("update_time")
    private String updateTime;

    @SerializedName("togglable")
    @JsonProperty("togglable")
    private Boolean togglable;

    @SerializedName("current_user_role_id")
    @JsonProperty("current_user_role_id")
    private Integer currentUserRoleId;

    @SerializedName("metadata")
    @JsonProperty("metadata")
    private Metadata metadata;

    @SerializedName("deleted")
    @JsonProperty("deleted")
    private Integer deleted;

    @SerializedName("owner_name")
    @JsonProperty("owner_name")
    private String ownerName;

    @SerializedName("project_id")
    @JsonProperty("project_id")
    private Integer projectId;

    @SerializedName("owner_id")
    @JsonProperty("owner_id")
    private Integer ownerId;

    @SerializedName("name")
    @JsonProperty("name")
    private String name;

    @SerializedName("repo_count")
    @JsonProperty("repo_count")
    private Integer repoCount;
}