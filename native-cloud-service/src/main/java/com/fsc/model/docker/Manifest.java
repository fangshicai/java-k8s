package com.fsc.model.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Manifest {

    @SerializedName("RepoTags")
    private List<String> repoTags;

    @SerializedName("Config")
    private String config;

    @SerializedName("Layers")
    private List<String> layers;
}