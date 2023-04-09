package com.fsc.service;

import com.fsc.model.form.AppForm;
import com.fsc.model.form.NameListForm;

public interface AppApiV1 {
    Object createApp(AppForm appForm);

    Object deleteApp(String nameSpace, NameListForm nameListForm);
}
