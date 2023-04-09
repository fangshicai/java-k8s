package com.fsc.model.response.harbor.manifest;

public class DockerManifest {
	private Manifest manifest;
	private String config;

	public void setManifest(Manifest manifest){
		this.manifest = manifest;
	}

	public Manifest getManifest(){
		return manifest;
	}

	public void setConfig(String config){
		this.config = config;
	}

	public String getConfig(){
		return config;
	}

	@Override
 	public String toString(){
		return 
			"DockerManifest{" +
			"manifest = '" + manifest + '\'' + 
			",config = '" + config + '\'' + 
			"}";
		}
}
