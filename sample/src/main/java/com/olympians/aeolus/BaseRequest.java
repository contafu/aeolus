package com.olympians.aeolus;

import com.olympians.aeolus.annotations.Query;

@Query
public class BaseRequest {

    private String name = "tangchao";
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
