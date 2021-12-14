package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Review;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.ReviewNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.ReviewRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping("/reviews")
    public String getAllReviews()
    {
        return new Gson().toJson(reviewRepository.findAll());
    }

    @GetMapping("/ratings/{rating}")
    public String getReviewsByRating(@PathVariable String rating)
    {
        return new Gson().toJson(reviewRepository.findByRating(rating));
    }

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public String addReview(@RequestBody Review review)
    {
        return new Gson().toJson(reviewRepository.save(review));
    }

    @PutMapping("/reviews/{id}")
    public String changeReview(@RequestBody Review newReview, @PathVariable int id)
    {
        try {
            reviewRepository.findById(newReview.getId()).map( review -> {
                review.setId(newReview.getId());
                review.setRating(newReview.getRating());
                review.setUsername(newReview.getUsername());
                review.setText(newReview.getText());

                return new Gson().toJson(reviewRepository.save(review));
            });
        } catch (ReviewNotFoundException exception)
        {
            return "Review not Found";
        }

        return "Something went wrong.";
    }

    @DeleteMapping("/reviews/{id}")
    public String deleteReview(@PathVariable int id)
    {
        reviewRepository.deleteById(id);
        return "Löschen erfolgreich";
    }
}
