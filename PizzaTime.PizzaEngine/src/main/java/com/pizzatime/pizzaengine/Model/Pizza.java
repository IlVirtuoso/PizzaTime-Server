package com.pizzatime.pizzaengine.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

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
    @ManyToMany
    private Set<Seasoning> seasonings;

    /** Una pizza dovrebbe essere indipendente dall'impasto
    @Expose
    @ManyToOne
    private Pastry pastry;

    public Pastry getPastry() {
    return pastry;
    }

    public void setPastry(Pastry pastry) {
    this.pastry = pastry;
    }
    */

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCommonName() { return commonName; }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public Set<Seasoning> getSeasonings() {
        return seasonings;
    }

    public void setSeasonings(Set<Seasoning> seasonings) {
        this.seasonings = seasonings;
    }

    public void addSeasoning(Seasoning s){
        seasonings.add(s);
    }

    public void removeSeasoning(Seasoning s){
        seasonings.remove(s);
    }

    public String toString(){return this.jsonfy();}

    public String jsonfy(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pizza pizza = (Pizza) o;
        return Objects.equals(id, pizza.id) && Objects.equals(commonName, pizza.commonName) && seasonings.equals(pizza.seasonings);
    }


    public boolean equalsSeasonings(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pizza pizza = (Pizza) o;
        //return Objects.equals(id, pizza.id) && Objects.equals(commonName, pizza.commonName) && seasonings.equals(pizza.seasonings);
        Set<Seasoning> seasoningsToCompare = ((Pizza) o).getSeasonings();
        if(seasoningsToCompare.containsAll(this.getSeasonings()) && (this.getSeasonings()).containsAll(seasoningsToCompare)){
            return true;
        }else{
            return false;
        }
    }

}
