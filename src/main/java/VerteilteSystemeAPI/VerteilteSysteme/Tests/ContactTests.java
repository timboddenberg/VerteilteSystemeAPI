package VerteilteSystemeAPI.VerteilteSysteme.Tests;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Contact;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAddContact() throws Exception {
        String jsonContactString = mvc.perform(get("/contact-requests/bycustomernumber/12345")).andReturn().getResponse().getContentAsString();

        int Id;

        if (! jsonContactString.equals("Contact Not Found"))
        {
            Gson gson = new Gson();
            Contact contact = gson.fromJson(jsonContactString, Contact.class);
            Id = contact.getId();

            mvc.perform(delete("/contact-requests/"+Id));
        }

        Contact contact = new Contact();
        Id = contact.getId();
        contact.setSubject("Fehler");
        contact.setDescription("Produkt kaputt");
        contact.setCustomerNumber("12345");
        contact.setFirstName("Tim");
        contact.setLastName("Boddenberg");
        contact.setEmail("t@b.de");
        contact.setCategory("Kuechengeraete");

        mvc.perform(post("/contact-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(contact))
                .accept(MediaType.APPLICATION_JSON));

        jsonContactString = mvc.perform(get("/contact-requests/bycustomernumber/12345").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Gson gson = new Gson();
        contact = gson.fromJson(jsonContactString, Contact.class);

        assertThat(contact.getSubject().equals("Fehler")).isTrue();
    }

    @Test
    public void testChangeContact() throws Exception {
        String jsonResult = mvc.perform(get("/contact-requests/bycustomernumber/12345")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        Contact contact = gson.fromJson(jsonResult, Contact.class);

        contact.setCategory("Computer");

        mvc.perform(put("/contact-requests/" + contact.getCustomerNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(contact).replace("Id","id"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        jsonResult = mvc.perform(get("/contact-requests/bycustomernumber/12345")).andReturn().getResponse().getContentAsString();
        contact = gson.fromJson(jsonResult, Contact.class);

        assertThat(contact.getCategory().equals("Computer")).isTrue();
    }

    @Test
    public void testDeleteContact() throws Exception {
        String jsonResult = mvc.perform(get("/contact-requests/bycustomernumber/12345")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        Contact contact = gson.fromJson(jsonResult, Contact.class);

        int id = contact.getId();

        mvc.perform(delete("/contact-requests/" + id));

        String response = mvc.perform(get("/contact-requests/bycustomernumber/12345").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        assertThat(response.equals("Contact Not Found")).isTrue();
    }

}
