package VerteilteSystemeAPI.VerteilteSysteme.Models;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserModel extends JpaRepository<User,Integer> {
}
