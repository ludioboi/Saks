package com.example.saks.api;

public class Person {

    String name, secondname, firstname, short_;
    int role, classid, id;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", secondname='" + secondname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", short_='" + short_ + '\'' +
                ", role=" + role +
                ", classid=" + classid +
                ", id=" + id +
                '}';
    }
}
