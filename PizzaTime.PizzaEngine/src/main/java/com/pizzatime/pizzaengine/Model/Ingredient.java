package com.pizzatime.pizzaengine.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Ingredient {
    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;
    @Expose
    private String commonName;

    @Expose
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "allergen_ingredient", joinColumns = @JoinColumn(name = "id"))
    private Set<Allergen> allergen;

    @Expose
    private String description;

    @Expose
    private Boolean glutenFree = true;


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCommonName() { return commonName; }

    public void setCommonName(String commonName) { this.commonName = commonName; }

    public Set<com.pizzatime.pizzaengine.Model.Allergen> getAllergen() {
        return allergen;
    }
    public String getDescription() {
        return description;
    }

    public Boolean getGlutenFree() {
        return glutenFree;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGlutenFree(Boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public void setAllergen(Set<com.pizzatime.pizzaengine.Model.Allergen> allergen) {
        this.allergen = allergen;
    }

    @Override
    public String toString() {
        return this.jsonfy();
    }

    public String jsonfy(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }


}
