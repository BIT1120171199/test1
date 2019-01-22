package com.bytedance.android.lesson.restapi.solution.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Xavier.S
 * @date 2019.01.17 18:08
 */
public class Cat {
    // TODO-C1 (1) Implement your Cat Bean here according to the response json
    @SerializedName("breeds")
    private List<?> breeds;
    @SerializedName("categories")
    private List<Categories> categories;
    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    public static class Categories {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        public int getId() {
            return this.id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public List<?> getBreeds() {
        return this.breeds;
    }
    public void setBreeds(List<?> breeds) {
        this.breeds = breeds;
    }
    public List<Categories> getCategories() {
        return this.categories;
    }
    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }
}
