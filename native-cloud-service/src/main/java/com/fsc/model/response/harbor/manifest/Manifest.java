package com.fsc.model.response.harbor.manifest;

import java.util.List;

public class Manifest{
	private int schemaVersion;
	private List<LayersItem> layers;
	private String mediaType;
	private Config config;

	public void setSchemaVersion(int schemaVersion){
		this.schemaVersion = schemaVersion;
	}

	public int getSchemaVersion(){
		return schemaVersion;
	}

	public void setLayers(List<LayersItem> layers){
		this.layers = layers;
	}

	public List<LayersItem> getLayers(){
		return layers;
	}

	public void setMediaType(String mediaType){
		this.mediaType = mediaType;
	}

	public String getMediaType(){
		return mediaType;
	}

	public void setConfig(Config config){
		this.config = config;
	}

	public Config getConfig(){
		return config;
	}

	@Override
 	public String toString(){
		return 
			"Manifest{" + 
			"schemaVersion = '" + schemaVersion + '\'' + 
			",layers = '" + layers + '\'' + 
			",mediaType = '" + mediaType + '\'' + 
			",config = '" + config + '\'' + 
			"}";
		}
}