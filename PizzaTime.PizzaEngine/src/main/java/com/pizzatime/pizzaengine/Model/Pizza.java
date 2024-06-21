package com.pizzatime.pizzaengine.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pizza {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;
    @Expose
    private String commonName;
    @Expose
    private String ingredientsList;



    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCommonName() { return commonName; }

    public String getIngredientsList() { return ingredientsList; }

    public void setCommonName(String commonName) { this.commonName = commonName; }

    public void setIngredientsList(String ingredientsList) { this.ingredientsList = ingredientsList; }

    public String jsonfy(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }


}
