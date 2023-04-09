package com.fsc.model.response.harbor.projects;

import lombok.Data;

import java.util.List;

@Data
public class ProjectList {
    private List<ProjectItem> projectItemList;
    private Integer total;
}
