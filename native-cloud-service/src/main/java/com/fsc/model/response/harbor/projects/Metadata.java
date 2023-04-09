package com.fsc.model.response.harbor.projects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Metadata{

	@SerializedName("public")
	@JsonProperty("public")
	private String jsonMemberPublic;
}