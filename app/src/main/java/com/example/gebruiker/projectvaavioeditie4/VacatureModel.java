package com.example.gebruiker.projectvaavioeditie4;

public class VacatureModel
{
    // Creating the strings for the module
    String Title, Locatie, Omschrijving, key;

    public VacatureModel(){

    }

    // Designing the VacatureModule, first declaring the type, the setting the corresponding variable
    public VacatureModel(String Title, String Locatie, String Omschrijving, String key) {
        this.Title = Title;
        this.Locatie = Locatie;
        this.Omschrijving = Omschrijving;
        this.key = key;
    }

}
