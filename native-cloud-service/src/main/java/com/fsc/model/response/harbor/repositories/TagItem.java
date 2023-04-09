package com.fsc.model.response.harbor.repositories;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagItem{

	@SerializedName("last_updated")
	private Object lastUpdated;

	@SerializedName("os")
	private String os;

	@SerializedName("signature")
	private Object signature;

	@SerializedName("author")
	private String author;

	@SerializedName("created")
	private String created;

	@SerializedName("docker_version")
	private String dockerVersion;

	@SerializedName("first_pushed")
	private Object firstPushed;

	@SerializedName("labels")
	private List<Object> labels;

	@SerializedName("size")
	private Integer size;

	@SerializedName("digest")
	private String digest;

	@SerializedName("name")
	private String name;

	@SerializedName("scan_overview")
	private ScanOverview scanOverview;

	@SerializedName("config")
	private Config config;

	@SerializedName("architecture")
	private String architecture;
}