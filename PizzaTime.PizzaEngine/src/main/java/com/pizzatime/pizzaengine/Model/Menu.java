package com.pizzatime.pizzaengine.Model;

import com.google.gson.annotations.Expose;
import com.pizzatime.pizzaengine.Service.GenericService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Entity
public class Menu {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Expose
    private Long pizzeriaId;

    @Expose
    @ManyToMany
    private Set<MenuRowPizza> pizzaRows;

    @Expose
    @ManyToMany
    private Set<MenuRowIngredient> ingrRows;

    @Expose
    private Boolean availlable = false;

    @Expose
    private Long availlableTimestamp = (long)-1;

    public Long getId() {
        return id;
    }

    public Long getPizzeriaId() {
        return pizzeriaId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPizzeriaId(Long pizzeriaId) {
        this.pizzeriaId = pizzeriaId;
    }

    public Set<MenuRowPizza> getPizzaRows() {
        return pizzaRows;
    }

    public Set<MenuRowIngredient> getIngrRows() {
        return ingrRows;
    }

    public void setPizzaRows(Set<MenuRowPizza> pizzaRows) {
        this.pizzaRows = pizzaRows;
    }

    public void setIngrRows(Set<MenuRowIngredient> ingrRows) {
        this.ingrRows = ingrRows;
    }

    public Boolean getAvaillable() {
        return availlable;
    }

    public Long getAvaillableTimestamp() {
        return availlableTimestamp;
    }

    public void setAvaillable(Boolean availlable) {
        this.availlable = availlable;
    }

    public void setAvaillableTimestamp(Long availlableTimestamp) {
        this.availlableTimestamp = availlableTimestamp;
    }
}
