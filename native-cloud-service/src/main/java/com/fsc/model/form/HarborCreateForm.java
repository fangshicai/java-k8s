package com.fsc.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import feign.Param;
import lombok.Data;

@Data
public class HarborCreateForm {
    @SerializedName("project_name")
    @JsonProperty("project_name")
    @Param(value = "project_name")
    private String projectName;

    @SerializedName("registry_id")
    @JsonProperty("registry_id")
    @Param(value = "registry_id")
    private String registryId;

    @SerializedName("storage_limit")
    @JsonProperty("storage_limit")
    @Param(value = "storage_limit")
    private Integer storageLimit;

    @SerializedName("metadata")
    @JsonProperty("metadata")
    private com.fsc.model.response.harbor.projects.Metadata metadata;


}
