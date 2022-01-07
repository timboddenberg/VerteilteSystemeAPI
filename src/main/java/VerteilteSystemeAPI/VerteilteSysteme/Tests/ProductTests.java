package VerteilteSystemeAPI.VerteilteSysteme.Tests;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Product;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAddAndGetProduct() throws Exception
    {
        String jsonProductString = mvc.perform(get("/products/byname/TestComputer")).andReturn().getResponse().getContentAsString();

        int Id;

        if (! jsonProductString.equals("Product Not Found"))
        {
            Gson gson = new Gson();
            Product product = gson.fromJson(jsonProductString, Product.class);
            Id = product.getId();

            mvc.perform(delete("/products/"+Id));
        }

        Product product = new Product();
        Id = product.getId();
        product.setName("TestComputer");
        product.setPrice(1000);
        product.setBrand("Dell");
        product.setSince(2021);
        product.setUrl("/Dell/TestComputer");

        mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(product))
                .accept(MediaType.APPLICATION_JSON));

        jsonProductString = mvc.perform(get("/products/byname/TestComputer").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Gson gson = new Gson();
        product = gson.fromJson(jsonProductString, Product.class);

        assertThat(product.getName().equals("TestComputer")).isTrue();
    }

    @Test
    public void testChangeProduct() throws Exception {
        String jsonResult = mvc.perform(get("/products/byname/TestComputer")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        Product product = gson.fromJson(jsonResult, Product.class);

        int Id = product.getId();

        product.setBrand("Alienware");

        mvc.perform(put("/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(product))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        jsonResult = mvc.perform(get("/products/" + Id)).andReturn().getResponse().getContentAsString();
        product = gson.fromJson(jsonResult, Product.class);

        assertThat(product.getBrand().equals("Alienware")).isTrue();
    }

    @Test
    public void testDeleteProduct() throws Exception
    {
        String jsonResult = mvc.perform(get("/products/byname/TestComputer")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        Product product = gson.fromJson(jsonResult, Product.class);

        int id = product.getId();

        mvc.perform(delete("/products/" + id));

        String response = mvc.perform(get("/products/byname/TestComputer").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        assertThat(response.equals("Product Not Found")).isTrue();
    }


}
