package com.fsc.model.response.harbor.repositories;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class RepositoryItem {

    @JsonProperty("creation_time")
    @SerializedName("creation_time")
    private String creationTime;

    @JsonProperty("update_time")
    @SerializedName("update_time")
    private String updateTime;

    @JsonProperty("max_tags_count")
    @SerializedName("max_tags_count")
    private Integer maxTagsCount;

    @JsonProperty("project_id")
    @SerializedName("project_id")
    private Integer projectId;

//    @Schema(description = "标签数")
    @JsonProperty("tags_count")
    @SerializedName("tags_count")
    private Integer tagsCount;

//    @Schema(description = "镜像名")
    @JsonProperty("name")
    @SerializedName("name")
    private String name;

//    @Schema(description = "描述")
    @JsonProperty("description")
    @SerializedName("description")
    private String description;

    @JsonProperty("id")
    @SerializedName("id")
    private Integer id;

    @JsonProperty("pull_count")
    @SerializedName("pull_count")
    private Integer pullCount;

    @JsonProperty("star_count")
    @SerializedName("star_count")
    private Integer starCount;

    @JsonProperty("labels")
    @SerializedName("labels")
    private List<String> labels;
    //关联软件列表
//    @JsonProperty("relateSoftwareList")
//    @SerializedName("relateSoftwareList")
//    private List<relateSoftwareInfo> relateSoftwareList;
    //最后更新tag
    @JsonProperty("lastVersion")
    @SerializedName("lastVersion")
    private String lastVersion;

    //最后更新用户名
    @JsonProperty("lastUserName")
    @SerializedName("lastUserName")
    private String lastUserName;

}