package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.User;
import VerteilteSystemeAPI.VerteilteSysteme.Models.UserModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserModel userModel;

    public UserController(UserModel userModel)
    {
        this.userModel = userModel;
    }

    @GetMapping("")
    public List<User> generateDatabaseIndex()
    {
        return this.userModel.findAll();
    }


}
