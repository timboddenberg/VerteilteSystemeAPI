package VerteilteSystemeAPI.VerteilteSysteme.Tests;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Orders;
import VerteilteSystemeAPI.VerteilteSysteme.Entities.Review;
import com.google.gson.Gson;
import org.junit.jupiter.api.Order;
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
public class OrderTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAddAndGetOrder() throws Exception
    {
        String jsonOrderString = mvc.perform(get("/orders/Computer")).andReturn().getResponse().getContentAsString();

        int Id;

        if (! jsonOrderString.equals("Order Not Found"))
        {
            Gson gson = new Gson();
            Orders order = gson.fromJson(jsonOrderString, Orders.class);
            Id = order.getId();

            mvc.perform(delete("/orders/"+Id));
        }

        Orders order = new Orders();
        order.setProduct("Computer");
        order.setQuantity(2);
        order.setPurchaser("timboddenberg");
        order.setTotalCosts(2000);

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(order))
                .accept(MediaType.APPLICATION_JSON));

        jsonOrderString = mvc.perform(get("/orders/Computer").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Gson gson = new Gson();
        order = gson.fromJson(jsonOrderString, Orders.class);

        assertThat(order.getProduct().equals("Computer")).isTrue();
    }

    @Test
    public void testChangeOrder() throws Exception {
        String jsonResult = mvc.perform(get("/orders/Computer")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        Orders order = gson.fromJson(jsonResult, Orders.class);

        order.setQuantity(3);

        mvc.perform(put("/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(order))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        jsonResult = mvc.perform(get("/orders/Computer")).andReturn().getResponse().getContentAsString();
        order = gson.fromJson(jsonResult, Orders.class);

        assertThat(order.getQuantity() == 3).isTrue();
    }

    @Test
    public void testDeleteOrder() throws Exception {
        String jsonResult = mvc.perform(get("/orders/Computer")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        Orders order = gson.fromJson(jsonResult, Orders.class);

        int id = order.getId();

        mvc.perform(delete("/orders/" + id));

        String response = mvc.perform(get("/orders/Computer").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        assertThat(response.equals("Order Not Found")).isTrue();
    }
}
