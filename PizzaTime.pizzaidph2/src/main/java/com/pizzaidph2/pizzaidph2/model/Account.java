package com.pizzaidph2.pizzaidph2.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.pizzaidph2.pizzaidph2.Component.Auth0JWT;
import com.pizzaidph2.pizzaidph2.Component.InvalidEmailFormatException;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
//@Table(name="account")
public class Account {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;
    @Expose
    //@Column(name = "firstName")
    private String firstName;
    @Expose
    //@Column(name="lastName")
    private String lastName;
    @Expose
    //@Column(name = "email", unique = true)
    private String email;
    @Expose
    //@Column(name="mobile")
    private String mobile;
    @Expose
    //@Column(name = "phone")
    private String phone;
    @Expose
    //@Column(name="address")
    private String address;

    @Expose
    private Boolean glutenPreference = false;

    @Expose
    private Boolean isRegistered = false;

    @Expose
    private Boolean isVendor = false;

    @Expose
    //@Column(name="username, unique = true")
    private String username;

    //@Column(name="password")
    private String password;
    @Expose
    //@Column(name="socialIdentity")
    private Boolean socialIdentity;

    @Expose
    private Float balance = (float)5.00;

    // NEL DB E' DA 255 CARATTERI... CAPIRE SE BASTA...
    // Bisognerebbe creare una nuova entità e fare un array... Capire se ho tempo
    private String sessionList;


    /*
    @Expose
    @ElementCollection
    //@Column(name="sessionID")
    private List<String> sessionID = new ArrayList<String>();
    @ElementCollection
    //@Column(name="jwt")
    private List<String> jwt =new ArrayList<String>();


    public List<String> getSessionID() {
        return sessionID;
    }

    public List<String> getJwt() {
        return jwt;
    }

    public void setSessionID(List<String> sessionID) {
        this.sessionID = sessionID;
    }

    public void setJwt(List<String> jwt) {
        this.jwt = jwt;
    }

    @Autowired
    Auth0JWT jwtUtility;

    public boolean addSession(String sessionToken){
        for(String old : sessionID){
            if(jwtUtility.verifySession(old)==null){
                sessionID.remove(old);
            }
        }
        sessionID.add(sessionToken);
        return true;
    }
    */

    /**
     * Object LoginIdentity - PROV
     * private LoginIdentity identities;
     */

    public Long getId() { return id; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) throws InvalidEmailFormatException {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()) {
            System.out.println("Email " + email + " makes sense");
            this.email = email;
        }else{
            throw new InvalidEmailFormatException("Invalid Email Format: "+ email);
        }
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getGlutenPreference() {
        return glutenPreference;
    }

    public void setGlutenPreference(Boolean glutenPreference) {
        this.glutenPreference = glutenPreference;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getSocialIdentity() {
        return socialIdentity;
    }

    public void setSocialIdentity(Boolean socialIdentity) {
        this.socialIdentity = socialIdentity;
    }

    public Boolean getRegistered() {
        return isRegistered;
    }

    public void setRegistered(Boolean registered) {
        isRegistered = registered;
    }

    public Boolean getVendor() {
        return isVendor;
    }

    public void setVendor(Boolean vendor) {
        isVendor = vendor;
    }

    public Float getBalance() { return balance; }

    public void setBalance(Float balance) { this.balance = balance; }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public String jsonfy(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    public Account makeSafeCopy(){
        Account a = new Account();
        a.setUsername(username);
        a.setAddress(address);
        try {
            a.setEmail(email);
        } catch (InvalidEmailFormatException e) {
            //This would never happen..
            e.printStackTrace();
        }
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setRegistered(isRegistered);
        a.setGlutenPreference(glutenPreference);
        a.setVendor(isVendor);
        a.setPhone(phone);
        a.setMobile(mobile);
        a.setId(id);
        a.setBalance(balance);
        return a;
    }

}

