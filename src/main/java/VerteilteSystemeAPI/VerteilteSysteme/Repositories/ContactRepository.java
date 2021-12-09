package VerteilteSystemeAPI.VerteilteSysteme.Repositories;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact,Integer> {

    public Contact findByCategory(String category);

    public Contact findByCustomerNumber(String customerNumber);
}
