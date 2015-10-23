package com.iagl.slimane.final_project.datasources;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by slimane on 24/09/2015.
 */
@DatabaseTable(tableName = "profile")
public class Profile {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(foreign = true)
    private Person person;

    @DatabaseField
    private boolean transaction_activated;

    @DatabaseField
    private float raduis;

    @DatabaseField
    private String url_picture;

    @DatabaseField
    private String bio;

    public Profile(){

    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isTransaction_activated() {
        return transaction_activated;
    }

    public void setTransaction_activated(boolean transaction_activated) {
        this.transaction_activated = transaction_activated;
    }

    public float getRaduis() {
        return raduis;
    }

    public void setRaduis(float raduis) {
        this.raduis = raduis;
    }

    public String getUrl_picture() {
        return url_picture;
    }

    public void setUrl_picture(String url_picture) {
        this.url_picture = url_picture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
