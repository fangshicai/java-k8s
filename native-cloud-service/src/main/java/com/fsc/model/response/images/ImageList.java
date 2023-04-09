package com.fsc.model.response.images;

import lombok.Data;

import java.util.List;

@Data
public class ImageList {
    private List<String> images;
    private Integer total;
}
