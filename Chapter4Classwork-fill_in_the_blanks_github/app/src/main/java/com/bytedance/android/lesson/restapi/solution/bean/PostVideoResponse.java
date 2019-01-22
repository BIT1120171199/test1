package com.bytedance.android.lesson.restapi.solution.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author Xavier.S
 * @date 2019.01.18 17:53
 */
public class PostVideoResponse {
    // TODO-C2 (3) Implement your PostVideoResponse Bean here according to the response json
    @SerializedName("success")
    private boolean success;
    @SerializedName("url")
    private String url;
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean isSuccess() {
        return this.success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
