package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Review;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.ReviewNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.ReviewRepository;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("Gibt alle Reviews aus dem Review Repository an, ohne diese zu filtern. Rückgabewert ist hier eine Liste als Json String formatiert.")
    public String getAllReviews()
    {
        return new Gson().toJson(reviewRepository.findAll());
    }

    @GetMapping("/ratings/{rating}")
    @ApiOperation("Gibt Reviews zurück, die dem gegebenen Rating entsprechen.")
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
    @ApiOperation("Gibt alle REviews zurück, bei dem der Username dem des übergebenen entspricht.")
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
    @ApiOperation("Fügt ein Review hinzu. Wenn dies gelingt, wird der Http-Statuscode 201 zurückgegeben.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addReview(@RequestBody Review review)
    {
        reviewRepository.save(review);
        return new ResponseEntity<>("HTTP/1.1 201 Created",HttpStatus.CREATED);
    }

    @PutMapping("/reviews/{id}")
    @ApiOperation("Ersetzt ein existierendes Review. Wenn die Operation gelingt, wird der Http-Statuscode 200 zurückgegeben, sonst 400.")
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
    @ApiOperation("Löscht ein Review anhand der gegebenen Id. Wenn dies gelingt, wird der Http-Statuscode 200 zurückgegeben.")
    public ResponseEntity<String> deleteReview(@PathVariable int id)
    {
        reviewRepository.deleteById(id);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }
}
