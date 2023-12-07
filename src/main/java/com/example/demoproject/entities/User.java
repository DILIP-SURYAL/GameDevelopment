package com.example.demoproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "name should not be blank and field is required")
    @Size(min = 2,max = 20,message = "name should be 2 to 20 character long")
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    private boolean isEnabled;
    private String imgURL;
    private String about;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Contact> list = new ArrayList<>();


    public User() {
    }

    public User(int id, String name, String email, String password, String role, boolean isEnabled, String imgURL, String about, List<Contact> list) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isEnabled = isEnabled;
        this.imgURL = imgURL;
        this.about = about;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Contact> getList() {
        return list;
    }

    public void setList(List<Contact> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", isEnabled=" + isEnabled +
                ", imgURL='" + imgURL + '\'' +
                ", about='" + about + '\'' +
                ", list=" + list +
                '}';
    }
}
