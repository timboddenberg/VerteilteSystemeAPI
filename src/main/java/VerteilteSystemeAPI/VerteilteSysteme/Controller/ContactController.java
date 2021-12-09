package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Contact;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.CategoryNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.UserNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.ContactRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository)
    {
        this.contactRepository = contactRepository;
    }

    @GetMapping("/contacts/contact-requests")
    public String getAllContactRequests()
    {
        // curl GET http://localhost:8080/contacts
        List<Contact> contactList = new ArrayList<>();

        if (contactRepository != null)
            contactList = contactRepository.findAll();

        return new Gson().toJson(contactList);
    }

    @GetMapping("/contacts/contact-requests/{category}")
    public String getSpecificContact(@PathVariable String category)
    {
        try{
            Contact specificContact = contactRepository.findByCategory(category);

            if (specificContact == null)
                throw new CategoryNotFoundException(category);

            return new Gson().toJson(specificContact);
        }
        catch (CategoryNotFoundException exception)
        {
            return "User Not Found";
        }
    }

    @PostMapping("/contacts/contact-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public Contact addContact(@RequestBody Contact contact)
    {
        // curl -H "Content-Type: application/json" -X POST http://localhost/user/add -d "{\"id\":\"1\",\"UserName\":\"Tim\", \"FirstName\":\"Tim\",\"LastName\":\"Boddenberg\",\"Email\":\"t@b.de\",\"Password\":\"12345\"}"
        // {"UserName":"timboddenberg","FirstName":"Tim","LastName":"Boddenberg","Email":"t@b.de","Password":"12345"}

        return contactRepository.save(contact);
    }

    @PutMapping("/contacts/{category}")
    @ResponseStatus(HttpStatus.CREATED)
    public String modifyContact(@RequestBody Contact newContact, @PathVariable String category)
    {
        try{
            Contact contact = contactRepository.findByCategory(newContact.getCustomerNumber());

            if (contact == null)
                throw new CategoryNotFoundException(category);

            return new Gson().toJson(contactRepository.save(contact));

        } catch (UserNotFoundException exception)
        {
            return "User not Found";
        }
    }

    @DeleteMapping("/contacts/{id}")
    public void deleteContact(@PathVariable int id)
    {
        contactRepository.deleteById(id);
    }

}
