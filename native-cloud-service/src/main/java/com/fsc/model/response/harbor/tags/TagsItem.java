package com.fsc.model.response.harbor.tags;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class TagsItem {
    @JsonProperty("addition_links")
    @SerializedName("addition_links")
    private Object additionLinks;

    @JsonProperty("digest")
    @SerializedName("digest")
    private String digest;

    @JsonProperty("extra_attrs")
    @SerializedName("extra_attrs")
    private Object extraAttrs;

    @JsonProperty("icon")
    @SerializedName("icon")
    private String icon;

    @JsonProperty("id")
    @SerializedName("id")
    private Integer id;

    @JsonProperty("labels")
    @SerializedName("labels")
    private String labels;

    @JsonProperty("manifest_media_type")
    @SerializedName("manifest_media_type")
    private String manifestMediaType;

    @JsonProperty("media_type")
    @SerializedName("media_type")
    private String mediaType;

    @JsonProperty("project_id")
    @SerializedName("project_id")
    private Integer projectId;

    @JsonProperty("pull_time")
    @SerializedName("pull_time")
    private String pullTime;

    @JsonProperty("push_time")
    @SerializedName("push_time")
    private String pushTime;

    @JsonProperty("references")
    @SerializedName("references")
    private String references;

    @JsonProperty("repository_id")
    @SerializedName("repository_id")
    private Integer repositoryId;

    @JsonProperty("size")
    @SerializedName("size")
    private Integer size;

    @JsonProperty("tags")
    @SerializedName("tags")
    private List<Tags> tags;

    @JsonProperty("type")
    @SerializedName("type")
    private String type;

}
