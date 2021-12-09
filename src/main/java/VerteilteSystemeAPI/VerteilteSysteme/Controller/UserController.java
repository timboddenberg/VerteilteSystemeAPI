package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.User;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.UserNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userModel;

    public UserController(UserRepository userModel)
    {
        userModel = userModel;
    }

    @GetMapping("/users")
    public String getAllUsers()
    {
        // curl GET http://localhost:8080/user
        List<User> userList = new ArrayList<>();

        if (userModel != null)
            userList = userModel.findAll();

        return new Gson().toJson(userList);
    }

    @GetMapping("/users/{id}")
    public String getSpecificUser(@PathVariable int id)
    {
        try{
            User specificUser = userModel.findById(id).orElseThrow(() -> new UserNotFoundException(id));
            return new Gson().toJson(specificUser);
        }
        catch (UserNotFoundException exception)
        {
            return "User Not Found";
        }
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user)
    {
        // curl -H "Content-Type: application/json" -X POST http://localhost/user/add -d "{\"id\":\"1\",\"UserName\":\"Tim\", \"FirstName\":\"Tim\",\"LastName\":\"Boddenberg\",\"Email\":\"t@b.de\",\"Password\":\"12345\"}"
        // {"UserName":"timboddenberg","FirstName":"Tim","LastName":"Boddenberg","Email":"t@b.de","Password":"12345"}

        return userModel.save(user);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public String modifyUser(@RequestBody User newUser, @PathVariable int id)
    {
        try{
            userModel.findById(newUser.getId()).map(user-> {
                user.setPassword(newUser.getPassword());
                user.setFirstName(newUser.getFirstName());
                user.setUserName(newUser.getUserName());
                user.setLastName(newUser.getLastName());
                user.setEmail(newUser.getEmail());
                return new Gson().toJson(userModel.save(user));
            }).orElseThrow(() -> new UserNotFoundException(newUser.getId()));
        } catch (UserNotFoundException exception)
        {
            return "User not Found";
        }

        return "Something went wrong.";
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id)
    {
        userModel.deleteById(id);
    }


}
