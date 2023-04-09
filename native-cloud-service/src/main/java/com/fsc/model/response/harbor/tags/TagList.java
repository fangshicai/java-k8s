package com.fsc.model.response.harbor.tags;

import lombok.Data;

import java.util.List;

@Data
public class TagList {
    private List<TagsItem> tagsItemList;
    private Integer total;
}
