package com.fsc.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppPort {
    private Integer containerPort;
    private String name;
    private String protocol;
}
