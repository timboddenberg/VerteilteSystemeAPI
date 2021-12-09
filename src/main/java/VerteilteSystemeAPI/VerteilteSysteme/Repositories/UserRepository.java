package VerteilteSystemeAPI.VerteilteSysteme.Repositories;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {}
