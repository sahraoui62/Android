package com.iagl.slimane.final_project.datasources;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by slimane on 18/09/2015.
 */
@DatabaseTable(tableName = "transactions")
public class Transaction {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(foreign = true, columnName="person_id")
    private Person person;

    @DatabaseField
    private String title;

    @DatabaseField
    private String description;

    @DatabaseField
    private String address_from;

    @DatabaseField
    private String address_to;

    @DatabaseField
    private float price;

    @DatabaseField
    private Transaction_State state;

    public Transaction(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress_from() {
        return address_from;
    }

    public void setAddress_from(String address_from) {
        this.address_from = address_from;
    }

    public String getAddress_to() {
        return address_to;
    }

    public void setAddress_to(String address_to) {
        this.address_to = address_to;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Transaction_State getState(){ return state; }

    public void setState(Transaction_State state){ this.state = state; }

}
