package VerteilteSystemeAPI.VerteilteSysteme.Exceptions;

public class ReviewNotFoundException extends RuntimeException{

    public ReviewNotFoundException()
    {
        super("Review not Found");
    }

}
