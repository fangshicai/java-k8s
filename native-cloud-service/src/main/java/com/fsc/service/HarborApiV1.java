package com.fsc.service;

import com.fsc.model.form.HarborCreateForm;
import com.fsc.model.request.query.HarborProjectQuery;
import com.fsc.model.response.harbor.projects.ProjectList;
import com.fsc.model.response.harbor.repositories.RepositoryList;
import com.fsc.model.response.harbor.tags.TagList;
import com.fsc.model.response.images.ImageList;
import feign.Param;

public interface HarborApiV1 {
    ProjectList projects(HarborProjectQuery query);

    RepositoryList repositories(String projectName, Integer page, Integer pageSize);

    TagList tags(String projectName, String repositoryName,Integer page,Integer pageSize);

    ImageList images(String projectName, String repositoryName, Integer page, Integer pageSize);

    String createProject(HarborCreateForm harborCreateForm);

    Boolean deleteProjectById(String projectId);
}
