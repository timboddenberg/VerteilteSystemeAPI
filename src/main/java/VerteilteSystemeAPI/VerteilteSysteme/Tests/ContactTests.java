package VerteilteSystemeAPI.VerteilteSysteme.Tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
//        mvc.perform(delete("/contact-requests/1"));
//        mvc.perform(
//            post("/contact-requests").contentType(MediaType.APPLICATION_JSON)
//                .content('')
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
    }

    @Test
    public void testGetContact() throws Exception {
        mvc.perform(get("/contact-requests")).andExpect(status().isOk());
    }

}
