package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.User;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.UserNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Models.UserModel;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserModel userModel;

    public UserController(UserModel userModel)
    {
        userModel = userModel;
    }

    @GetMapping("/user")
    public String getAllUsers()
    {
        // curl GET  http://localhost:8080/user
        List<User> userList = new ArrayList<>();

        if (userModel != null)
            userList = userModel.findAll();

        return new Gson().toJson(userList);
    }

    @GetMapping("/user/{id}")
    public User getSpecificUser(@PathVariable int id)
    {
        return userModel.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/user/add")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user)
    {
        // curl -H "Content-Type: application/json" -X POST http://localhost/user/add -d "{\"id\":\"1\",\"UserName\":\"Tim\", \"FirstName\":\"Tim\",\"LastName\":\"Boddenberg\",\"Email\":\"t@b.de\",\"Password\":\"12345\"}"
        // {"UserName":"timboddenberg","FirstName":"Tim","LastName":"Boddenberg","Email":"t@b.de","Password":"12345"}

        return userModel.save(user);
    }



}
