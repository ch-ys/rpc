package org.example.model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;

    public String getUserName(){
        return name;
    }

    public void setUserName(String userName){
        this.name = userName;
    }
}
