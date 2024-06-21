package com.pizzaidph2.pizzaidph2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
//@Table(name="customer")
public class Customer extends Account{

    //@Column(name = "glutenPreference")
    private boolean glutenPreference;

    public boolean isGlutenPreference() {
        return glutenPreference;
    }

    public void setGlutenPreference(boolean glutenPreference) {
        this.glutenPreference = glutenPreference;
    }
}
