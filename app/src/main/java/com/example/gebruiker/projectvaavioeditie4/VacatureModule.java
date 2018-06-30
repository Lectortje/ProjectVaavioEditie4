package com.example.gebruiker.projectvaavioeditie4;

public class VacatureModule
{
    // Creating the strings for the module
    public String Functie, Locatie, Omschrijving, key;

    public VacatureModule()
    {

    }

    // Designing the VacatureModule, first declaring the type, the setting the corresponding variable
    public VacatureModule(String Functie, String Locatie, String Omschrijving, String key)
    {
        this.Functie = Functie;
        this.Locatie = Locatie;
        this.Omschrijving = Omschrijving;
        this.key = key;
    }

}
