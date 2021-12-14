package VerteilteSystemeAPI.VerteilteSysteme.Repositories;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

    List<Orders> findByProduct(String product);

}
