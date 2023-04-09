package com.fsc.model.request.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import feign.Param;
import lombok.Data;

import java.io.Serializable;

@Data
public class HarborProjectQuery implements Serializable {

    private String RequestId;

    private Integer page;

    @SerializedName("page_size")
    @JsonProperty("public")
    @Param(value = "page_size")
    private Integer pageSize;

    private String name;

    @JsonProperty("public")
    @SerializedName("public")
    @Param(value = "public")
    private Boolean publicStr;

    private String owner;

    @JsonProperty("with_detail")
    @SerializedName("with_detail")
    @Param(value = "with_detail")
    private Boolean withDetail;

}
