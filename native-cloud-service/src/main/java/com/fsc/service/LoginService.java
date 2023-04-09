package com.fsc.service;

import com.fsc.model.R;
import com.fsc.model.form.UserLoginForm;

public interface LoginService {
    R login(UserLoginForm user);
    R logOut();
}
