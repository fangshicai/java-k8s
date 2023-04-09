package com.fsc.model.response.harbor.repositories;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Components{

	@SerializedName("summary")
	private List<Object> summary;

	@SerializedName("total")
	private Integer total;
}