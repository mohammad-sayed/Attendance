package com.ms.checkin;

import java.io.Serializable;

/**
 * Created by Mohammad-Sayed-PC on 12/14/2015.
 */
public class User implements Serializable {

    private String id;
    private String userName;
    private boolean checkedIn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
