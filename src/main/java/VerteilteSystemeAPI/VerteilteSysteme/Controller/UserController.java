package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.User;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.UserNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.UserRepository;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController{

    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userModel)
    {
        userModel = userModel;
    }

    @GetMapping(value = "/users", produces = "application/json")
    @ApiOperation(value = "Gibt alle Nutzer aus dem User Repository an, ohne diese zu filtern. Rückgabewert ist hier eine Liste als Json String formatiert.")
    public String getAllUsers()
    {
        return new Gson().toJson(userRepository.findAll());
    }

    @GetMapping(value = "/users/{id}", produces = "application/json")
    @ApiOperation(value = "Gibt einen Nutzer anhand seiner Id zurück. Da die Id unique ist, gibt es immer nur einen Nutzer als Json formatiertes Objekt zurück.")
    public String getSpecificUser(@PathVariable int id)
    {
        try{
            User specificUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
            return new Gson().toJson(specificUser);
        }
        catch (UserNotFoundException exception)
        {
            return "User Not Found";
        }
    }

    @GetMapping(value = "/users/byname/{name}", produces = "application/json")
    @ApiOperation(value = "Gibt einen Nutzer anhand seines Namens zurück. Zurtst wird der Vorname überprüft, danach der Nachname. Rückgabewert ist ein Json formatiertes Objekt.")
    public String getSpecificUserByName(@PathVariable String name)
    {
        try{
            List<User> userListFirstName = userRepository.findByFirstName(name);

            if (! userListFirstName.isEmpty())
                return new Gson().toJson(userListFirstName.get(0));

            List<User> userListLastName = userRepository.findByLastName(name);

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
    @ApiOperation(value = "Fügt einen Nutzer hinzu. Rückgabewert ist hier beim erfolgreichen anlegen der Http-Statuscode 201.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addUser(@RequestBody User user)
    {
        userRepository.save(user);
        return new ResponseEntity<>("HTTP/1.1 201 Created",HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    @ApiOperation("Überschreibt einen Nutzer. Sollte der Vorgang erfolgreich sein, wird der Http-Statuscode 201 zurückgegeben, sonst 400.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> modifyUser(@RequestBody User newUser, @PathVariable int id)
    {
        try{
            userRepository.findById(newUser.getId()).map(user-> {
                user.setPassword(newUser.getPassword());
                user.setFirstName(newUser.getFirstName());
                user.setUserName(newUser.getUserName());
                user.setLastName(newUser.getLastName());
                user.setEmail(newUser.getEmail());

                userRepository.save(user);
                return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
            }).orElseThrow(() -> new UserNotFoundException(newUser.getId()));

            return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
        } catch (UserNotFoundException exception)
        {
            return new ResponseEntity<>("HTTP/1.1 400 Bad Request - User Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users/{id}")
    @ApiOperation("Löscht einen Nutzer anhand seiner Id. Wenn das Löschen erfolgreich ist, wird der Http-Statuscode 200 zurückgegeben.")
    public ResponseEntity<String> deleteUser(@PathVariable int id)
    {
        userRepository.deleteById(id);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }

}
