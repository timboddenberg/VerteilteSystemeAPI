package VerteilteSystemeAPI.VerteilteSysteme.Tests;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.User;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MimeType;

import static org.assertj.core.api.Assertions.assertThat;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAddAndGetUser() throws Exception
    {
        String jsonUserString = mvc.perform(get("/users/byname/Boddenberg")).andReturn().getResponse().getContentAsString();

        int Id;

        if (! jsonUserString.equals("User Not Found"))
        {
            Gson gson = new Gson();
            User user = gson.fromJson(jsonUserString, User.class);
            Id = user.getId();

            mvc.perform(delete("/users/"+Id));
        }

        User user = new User();
        Id = user.getId();
        user.setUserName("timboddenberg");
        user.setFirstName("Tim");
        user.setLastName("Boddenberg");
        user.setEmail("t@b.de");
        user.setPassword("123456");

        mvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(user))
                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated());

        jsonUserString = mvc.perform(get("/users/byname/Boddenberg").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Gson gson = new Gson();
        user = gson.fromJson(jsonUserString, User.class);

        assertThat(user.getUserName().equals("timboddenberg")).isTrue();
    }

    @Test
    public void testChangeUser() throws Exception
    {
        String jsonResult = mvc.perform(get("/users/byname/Boddenberg")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        User user = gson.fromJson(jsonResult, User.class);

        int id = user.getId();

        user.setPassword("1");

        mvc.perform(put("/users/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        jsonResult = mvc.perform(get("/users/" + id)).andReturn().getResponse().getContentAsString();
        user = gson.fromJson(jsonResult, User.class);

        assertEquals("1",user.getPassword(),true);
    }

    @Test
    public void testDeleteUser() throws Exception
    {
        String jsonResult = mvc.perform(get("/users/byname/Boddenberg")).andReturn().getResponse().getContentAsString();
        assertThat(jsonResult).isNotNull();

        Gson gson = new Gson();
        User user = gson.fromJson(jsonResult, User.class);

        int id = user.getId();

        mvc.perform(delete("/users/" + id));

        String response = mvc.perform(get("/users/" + id).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        assertThat(response.equals("User Not Found")).isTrue();
    }
}
