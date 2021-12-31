package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.User;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.UserNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController{

    @Autowired
    private UserRepository userModel;

    public UserController(UserRepository userModel)
    {
        userModel = userModel;
    }

    @GetMapping("/users")
    public String getAllUsers()
    {
        return new Gson().toJson(userModel.findAll());
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

    @GetMapping("/users/byname/{name}")
    public String getSpecificUserByName(@PathVariable String name)
    {
        try{
            List<User> userListFirstName = userModel.findByFirstName(name);

            if (! userListFirstName.isEmpty())
                return new Gson().toJson(userListFirstName.get(0));

            List<User> userListLastName = userModel.findByLastName(name);

            if (!userListLastName.isEmpty())
                return new Gson().toJson(userListLastName.get(0));

            throw new UserNotFoundException(0);
        }
        catch (UserNotFoundException exception)
        {
            return "User Not Found";
        }
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addUser(@RequestBody User user)
    {
        // Json:
        // {"UserName":"timboddenberg","FirstName":"Tim","LastName":"Boddenberg","Email":"t@b.de","Password":"12345"}
        userModel.save(user);
        return new ResponseEntity<>("HTTP/1.1 201 Created",HttpStatus.CREATED);
    }

    @PutMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> modifyUser(@RequestBody User newUser)
    {
        try{
            userModel.findById(newUser.getId()).map(user-> {
                user.setPassword(newUser.getPassword());
                user.setFirstName(newUser.getFirstName());
                user.setUserName(newUser.getUserName());
                user.setLastName(newUser.getLastName());
                user.setEmail(newUser.getEmail());

                userModel.save(user);
                return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
            }).orElseThrow(() -> new UserNotFoundException(newUser.getId()));

            return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
        } catch (UserNotFoundException exception)
        {
            return new ResponseEntity<>("HTTP/1.1 400 Bad Request - User Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id)
    {
        userModel.deleteById(id);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }

}
