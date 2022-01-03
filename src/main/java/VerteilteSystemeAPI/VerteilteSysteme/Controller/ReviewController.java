package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Review;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.ReviewNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.ReviewRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController{

    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping("/reviews")
    public String getAllReviews()
    {
        return new Gson().toJson(reviewRepository.findAll());
    }

    @GetMapping("/ratings/{rating}")
    public String getReviewsByRating(@PathVariable int rating)
    {
        try{
            List<Review> reviewList = reviewRepository.findByRating(rating);

            if (reviewList.isEmpty())
                throw new ReviewNotFoundException();

            return new Gson().toJson(reviewList.get(0));
        } catch (ReviewNotFoundException exception) {
            return "Review Not Found";
        }
    }

    @GetMapping("/reviews/{username}")
    public String getReviewsById(@PathVariable String username)
    {
        try{
            List<Review> reviewList = reviewRepository.findByUsername(username);

            if (reviewList.isEmpty())
                throw new ReviewNotFoundException();

            return new Gson().toJson(reviewList.get(0));
        } catch (ReviewNotFoundException exception)
        {
            return "Review Not Found";
        }
    }

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addReview(@RequestBody Review review)
    {
        reviewRepository.save(review);
        return new ResponseEntity<>("HTTP/1.1 201 Created",HttpStatus.CREATED);
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<String> changeReview(@RequestBody Review newReview, @PathVariable int id)
    {
        try {
            reviewRepository.findById(newReview.getId()).map( review -> {
                review.setId(newReview.getId());
                review.setRating(newReview.getRating());
                review.setUsername(newReview.getUsername());
                review.setText(newReview.getText());

                reviewRepository.save(review);
                return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
            }).orElseThrow(ReviewNotFoundException::new);

            return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
        } catch (ReviewNotFoundException exception)
        {
            return new ResponseEntity<>("HTTP/1.1 400 Bad Request - Review Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable int id)
    {
        reviewRepository.deleteById(id);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }
}
