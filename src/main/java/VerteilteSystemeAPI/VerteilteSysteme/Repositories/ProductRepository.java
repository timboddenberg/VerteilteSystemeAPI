package VerteilteSystemeAPI.VerteilteSysteme.Repositories;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {}
