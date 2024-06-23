package com.pizzatime.pizzaengine.Model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

@Entity
public class MenuRowIngredient{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long rowid;

    @Expose
    private Float cost;

    @Expose
    private String commonName;

    @Expose
    @ManyToOne
    @JoinColumn(name="id")
    private Ingredient ingredient;

    public Long getRowid() {
        return rowid;
    }

    public Float getCost() {
        return cost;
    }

    public String getCommonName() {
        return commonName;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setRowid(Long rowid) {
        this.rowid = rowid;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
