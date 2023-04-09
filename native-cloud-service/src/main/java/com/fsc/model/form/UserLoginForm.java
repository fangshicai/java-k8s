package com.fsc.model.form;

import lombok.Data;

@Data
public class UserLoginForm {
    private String username;
    private String password;
    private String captcha;
}
