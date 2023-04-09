package com.fsc.feign;

import com.fsc.model.form.HarborCreateForm;
import com.fsc.model.request.query.HarborProjectQuery;
import com.fsc.model.response.harbor.projects.ProjectItem;
import com.fsc.model.response.harbor.repositories.RepositoryItem;
import com.fsc.model.response.harbor.tags.TagsItem;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface Harbor {

    @RequestLine("GET /api/v2.0/projects")
    List<ProjectItem> getProjects(@QueryMap HarborProjectQuery query);

    @RequestLine("GET /api/v2.0/projects/{project_name}/repositories")
    List<RepositoryItem> getRepositories(@Param("project_name") String projectName, @QueryMap Map<String, Object> map);

    @RequestLine("GET /api/v2.0/projects/{project_name}/repositories/{repository_name}/artifacts")
    List<TagsItem> getTag(@Param("project_name") String projectName, @Param("repository_name") String repositoryName, @QueryMap Map<String, Object> map);

    @RequestLine("POST  /api/v2.0/projects")
    @Headers("Content-Type: application/json")
    Object createProject(@RequestBody HarborCreateForm harborCreateForm);

    @RequestLine("DELETE /api/v2.0/projects/{project_name_or_id}")
    Object deleteProjectById(@Param("project_name_or_id") String projectId);
}
