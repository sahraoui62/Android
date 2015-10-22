package com.iagl.slimane.final_project.datasources;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by slimane on 14/09/2015.
 */

@DatabaseTable(tableName = "person")
public class Person {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String firstname;
    @DatabaseField
    private String lastname;
    @DatabaseField
    private String address;
    @DatabaseField
    private String phone_number;
    @DatabaseField
    private String password;
    @DatabaseField
    private String username;

    public Person(){

    }

    public Person(String firstname, String lastname, String address, String phone_number, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone_number = phone_number;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
