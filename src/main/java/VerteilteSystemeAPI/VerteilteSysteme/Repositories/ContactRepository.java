package VerteilteSystemeAPI.VerteilteSysteme.Repositories;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Integer> {

    List<Contact> findByCategory(String category);

    List<Contact> findByCustomerNumber(String customerNumber);
}
