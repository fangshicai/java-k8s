package com.fsc.model.response.harbor.manifest;

public class LayersItem{
	private Long size;
	private String digest;
	private String mediaType;

	public void setSize(Long size){
		this.size = size;
	}

	public Long getSize(){
		return size;
	}

	public void setDigest(String digest){
		this.digest = digest;
	}

	public String getDigest(){
		return digest;
	}

	public void setMediaType(String mediaType){
		this.mediaType = mediaType;
	}

	public String getMediaType(){
		return mediaType;
	}

	@Override
 	public String toString(){
		return 
			"LayersItem{" + 
			"size = '" + size + '\'' + 
			",digest = '" + digest + '\'' + 
			",mediaType = '" + mediaType + '\'' + 
			"}";
		}
}
