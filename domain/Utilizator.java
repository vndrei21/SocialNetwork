package com.example.lab8.domain;


import java.util.LinkedList;
import java.util.Objects;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private LinkedList<Utilizator> friends;

    /**
     *
     * @param firstName
     * @param lastName
     */
    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new LinkedList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void SaveInList(Utilizator utilizator){
        this.friends.add(utilizator);
    }
    public void deletefromlist(Utilizator id)
    {
        this.friends.remove(id);
    }

    public LinkedList<Utilizator> getFriends() {
        return friends;
    }
    public  void setFriends(LinkedList<Utilizator> user)
    {
        this.friends = user;
    }

    @Override
    public String toString() {

        String friends="";
        for (Utilizator friend:this.friends) {
            friends=friends+friend.getFirstName()+friend.getLastName();

        }
        return "Utilizator{" +"id "+this.getId()+
                " firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" +  friends+
                '}';
    }
}