package VerteilteSystemeAPI.VerteilteSysteme.Tests;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Review;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAddAndGetReview() throws Exception {
        String jsonReviewString = mvc.perform(get("/reviews/timboddenberg")).andReturn().getResponse().getContentAsString();

        int Id;

        if (! jsonReviewString.equals("Review Not Found"))
        {
            Gson gson = new Gson();
            Review review = gson.fromJson(jsonReviewString, Review.class);
            Id = review.getId();

            mvc.perform(delete("/reviews/"+Id));
        }

        Review review = new Review();
        Id = review.getId();
        review.setRating(5);
        review.setUsername("timboddenberg");
        review.setText("gutes Produkt.");

        mvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(review))
                .accept(MediaType.APPLICATION_JSON));

        jsonReviewString = mvc.perform(get("/reviews/timboddenberg").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Gson gson = new Gson();
        review = gson.fromJson(jsonReviewString, Review.class);

        assertThat(review.getUsername().equals("timboddenberg")).isTrue();
    }

    @Test
    public void testChangeReview() throws Exception {
        String jsonResult = mvc.perform(get("/reviews/timboddenberg")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        Review review = gson.fromJson(jsonResult, Review.class);

        review.setText("schlechtes Produkt.");

        mvc.perform(put("/reviews/" + review.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(review))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        jsonResult = mvc.perform(get("/reviews/timboddenberg")).andReturn().getResponse().getContentAsString();
        review = gson.fromJson(jsonResult, Review.class);

        assertThat(review.getText().equals("schlechtes Produkt.")).isTrue();
    }

    @Test
    public void testDeleteReview() throws Exception {
        String jsonResult = mvc.perform(get("/reviews/timboddenberg")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        Review review = gson.fromJson(jsonResult, Review.class);

        int id = review.getId();

        mvc.perform(delete("/reviews/" + id));

        String response = mvc.perform(get("/reviews/timboddenberg").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        assertThat(response.equals("Review Not Found")).isTrue();
    }
}
