package com.seebon.config;

import com.seebon.config.callback.ConfigChangeContent;

public class CallBackDemoTest {

    public CallBackDemoTest() {
        ConfigChangeContent.getInstance().setCustomCallBack(new MyCallBackDemo());
        System.setProperty("see.config.client.appName", "module-system");
    }
}
