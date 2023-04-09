package com.fsc.service.impl;

import com.fsc.config.HarborConfigProperties;
import com.fsc.feign.Harbor;
import com.fsc.model.form.HarborCreateForm;
import com.fsc.model.request.query.HarborProjectQuery;
import com.fsc.model.response.harbor.projects.ProjectItem;
import com.fsc.model.response.harbor.projects.ProjectList;
import com.fsc.model.response.harbor.repositories.RepositoryItem;
import com.fsc.model.response.harbor.repositories.RepositoryList;
import com.fsc.model.response.harbor.tags.TagList;
import com.fsc.model.response.harbor.tags.TagsItem;
import com.fsc.model.response.images.ImageList;
import com.fsc.service.HarborApiV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HarborApiV1Impl implements HarborApiV1 {

    @Autowired
    private Harbor harbor;

    @Autowired
    private HarborConfigProperties harborConfigProperties;

    /**
     * 获取项目列表
     *
     * @param query
     * @return
     */
    @Override
    public ProjectList projects(HarborProjectQuery query) {
        List<ProjectItem> projects = harbor.getProjects(query);
        ProjectList repositoryList = new ProjectList();
        repositoryList.setProjectItemList(projects);
        repositoryList.setTotal(projects.size());
        return repositoryList;
    }

    /**
     * 获取项目下的仓库
     *
     * @param projectName
     * @return
     */
    @Override
    public RepositoryList repositories(String projectName, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (page != null) {
            map.put("page", page);
        }
        if (pageSize != null) {
            map.put("page_size", pageSize);
        }

        RepositoryList repositoryList = new RepositoryList();
        List<RepositoryItem> repositories = harbor.getRepositories(projectName, map);
        repositoryList.setList(repositories);
        repositoryList.setTotal(repositories.size());
        return repositoryList;
    }

    /**
     * 获取镜像下的tag
     *
     * @param projectName
     * @param repositoryName
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public TagList tags(String projectName, String repositoryName, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (page != null) {
            map.put("page", page);
        }
        if (pageSize != null) {
            map.put("page_size", pageSize);
        }

        List<TagsItem> tagsItems = harbor.getTag(projectName, repositoryName, map);
        TagList tagList = new TagList();
        tagList.setTagsItemList(tagsItems);
        tagList.setTotal(tagsItems.size());
        return tagList;
    }

    @Override
    public Boolean deleteProjectById(String projectId) {
        harbor.deleteProjectById(projectId);
        return true;
    }

    /**
     * 获取harbor镜像
     *
     * @param projectName
     * @param repositoryName
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public ImageList images(String projectName, String repositoryName, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        if (page != null) {
            map.put("page", page);
        }
        if (pageSize != null) {
            map.put("page_size", pageSize);
        }


        List<TagsItem> tagsItems = harbor.getTag(projectName, repositoryName, map);
        // 获取tag
        List<String> tags = tagsItems.stream().map(o -> harborConfigProperties.getHarborIp() + "/" + projectName + "/" + repositoryName + ":" + o.getTags().get(0).getName()).collect(Collectors.toList());

        ImageList imageList = new ImageList();
        imageList.setImages(tags);
        imageList.setTotal(tags.size());
        return imageList;
    }


    @Override
    public String createProject(HarborCreateForm harborCreateForm) {
        harbor.createProject(harborCreateForm);
        return "ok";
    }
}
