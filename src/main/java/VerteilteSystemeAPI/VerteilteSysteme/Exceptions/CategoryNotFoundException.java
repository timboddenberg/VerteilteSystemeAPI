package VerteilteSystemeAPI.VerteilteSysteme.Exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String category)
    {
        super("Category " + category + " not found!");
    }
}
