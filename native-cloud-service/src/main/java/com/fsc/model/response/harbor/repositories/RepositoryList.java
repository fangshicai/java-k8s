package com.fsc.model.response.harbor.repositories;

import lombok.Data;

import java.util.List;

@Data
public class RepositoryList {
    private List<RepositoryItem> list;
    private long total;
}
