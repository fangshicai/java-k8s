package com.fsc.model.response.harbor.manifest;

import lombok.Data;

@Data
public class Config {
    private int size;
    private String digest;

    private String mediaType;
}
