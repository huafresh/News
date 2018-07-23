package com.example.hua.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @version 2018/4/8 9:20
 */

public class testssss {

    private String name;
    private List<String> list;


    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
