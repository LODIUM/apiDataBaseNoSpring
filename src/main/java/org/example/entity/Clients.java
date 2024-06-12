package org.example.entity;

public class Clients {
    private  int id;
    private String first_name;
    private String last_name;

    private String number_phone;
    private String email;
    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber_phone() {
        return number_phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setNumberPhone(String number_phone) {
        this.number_phone = number_phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


