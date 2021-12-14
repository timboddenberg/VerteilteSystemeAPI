package VerteilteSystemeAPI.VerteilteSysteme.Exceptions;

public class ContactNotFoundException extends RuntimeException
{
    public ContactNotFoundException(String customerNumber)
    {
        super("Contact with customer number " + customerNumber + " not found!");
    }
}
