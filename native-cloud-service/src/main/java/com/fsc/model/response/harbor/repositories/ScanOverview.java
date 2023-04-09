package com.fsc.model.response.harbor.repositories;

import com.google.gson.annotations.SerializedName;

public class ScanOverview{

	@SerializedName("severity")
	private Integer severity;

	@SerializedName("creation_time")
	private String creationTime;

	@SerializedName("components")
	private Components components;

	@SerializedName("update_time")
	private String updateTime;

	@SerializedName("image_digest")
	private String imageDigest;

	@SerializedName("scan_status")
	private String scanStatus;

	@SerializedName("job_id")
	private Integer jobId;

	@SerializedName("details_key")
	private String detailsKey;
}