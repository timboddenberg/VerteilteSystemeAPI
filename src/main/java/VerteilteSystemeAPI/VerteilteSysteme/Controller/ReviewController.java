package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Review;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.ReviewNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.ReviewRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String getReviewsByRating(@PathVariable String rating)
    {
        return new Gson().toJson(reviewRepository.findByRating(rating));
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
