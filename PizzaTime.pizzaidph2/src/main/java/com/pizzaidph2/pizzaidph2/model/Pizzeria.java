package com.pizzaidph2.pizzaidph2.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

@Entity
//@Table(name="Pizzeria")
public class Pizzeria{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Expose
    private String name;

    private Boolean availlability = false;

    @Expose
    private Long managerId;

    @Expose
    private String vatNumber;

    @Expose
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAvaillability() {
        return availlability;
    }

    public void setAvaillability(boolean availlability) {
        this.availlability = availlability;
    }


    public Boolean getAvaillability() {
        return availlability;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setAvaillability(Boolean availlability) {
        this.availlability = availlability;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String jsonfy(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

}
