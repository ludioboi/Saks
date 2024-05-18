package com.example.saks.search;

import com.google.gson.internal.LinkedTreeMap;

public class SearchUserEntry {

    private String firstname,
     secondname,
     lastname,
     id,
     short_name,
     role;

    public SearchUserEntry(String firstname, String secondname, String lastname, String id, String short_name, String role) {
        this.firstname = firstname;
        this.secondname = secondname;
        this.lastname = lastname;
        this.id = id;
        this.short_name = short_name;
        this.role = role;
    }

    public SearchUserEntry(LinkedTreeMap<String, Object> data) {
        this.firstname = (String) data.get("firstname");
        this.secondname = (String) data.get("secondname");
        this.lastname = (String) data.get("lastname");
        this.id = String.valueOf(Math.round(Double.parseDouble(data.get("id").toString())));
        this.short_name = (String) data.get("short_name");
        this.role = String.valueOf(Math.round(Double.parseDouble(data.get("role").toString())));
    }

    public String getFullName(){
        return firstname + " " + (secondname != null ? " " + secondname + " ": "")+ lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShort_name() {
        return short_name != null ? short_name : "";
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getRole() {
        return role;
    }

    public String getRoleName(){
        switch (role) {
            case "1":
                return "Schüler";
            case "2":
                return "Lehrer";
            case "3":
                return "Admin";
            default:
                return "Unbekannt";
        }
    }

    public void setRole(String role) {
        this.role = role;
    }
}
