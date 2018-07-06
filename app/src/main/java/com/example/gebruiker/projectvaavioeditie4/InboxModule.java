package com.example.gebruiker.projectvaavioeditie4;

public class InboxModule
{
    // Creating the strings for the module
    public String Verzender, Bericht, Onderwerp,  Key;

    public InboxModule()
    {

    }

    // Designing the InboxModule, first declaring the type, the setting the corresponding variable
    public InboxModule(String Verzender, String Bericht, String Onderwerp,  String Key)
    {
        this.Verzender = Verzender;
        this.Bericht = Bericht;
        this.Onderwerp = Onderwerp;
        this.Key = Key;
    }

}
