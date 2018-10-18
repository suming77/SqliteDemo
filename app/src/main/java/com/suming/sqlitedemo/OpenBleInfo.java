package com.suming.sqlitedemo;

/**
 * @创建者 mingyan.su
 * @创建时间 2018/9/29 18:03
 * @类描述 ${TODO}
 */
public class OpenBleInfo {
    private long user_id;
    private String name;
    private String address;
    private String time;
    private String phone;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
