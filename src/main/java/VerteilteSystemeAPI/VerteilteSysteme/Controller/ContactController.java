package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Contact;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.CategoryNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.ContactNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.UserNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.ContactRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ContactController{

    @Autowired
    private final ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository)
    {
        this.contactRepository = contactRepository;
    }

    @GetMapping("/contact-requests")
    public String getAllContactRequests()
    {
        List<Contact> contactList = new ArrayList<>();

        if (contactRepository != null)
            contactList = contactRepository.findAll();

        return new Gson().toJson(contactList);
    }

    @GetMapping("/contact-requests/{category}")
    public String getSpecificContact(@PathVariable String category)
    {
        try{
            List<Contact> specificContact = contactRepository.findByCategory(category);

            if (specificContact.isEmpty())
                throw new CategoryNotFoundException(category);

            return new Gson().toJson(specificContact.get(0));
        }
        catch (CategoryNotFoundException exception)
        {
            return "Category Not Found.";
        }
    }

    @GetMapping("/contact-requests/bycustomernumber/{customerNumber}")
    public String getSpecificContactByCustomerNumber(@PathVariable String customerNumber)
    {
        try {
            List<Contact> contactList = contactRepository.findByCustomerNumber(customerNumber);

            if (! contactList.isEmpty())
                return new Gson().toJson(contactList.get(0));

            throw new ContactNotFoundException("0");

        } catch (ContactNotFoundException exception)
        {
            return "Contact Not Found";
        }
    }

    @PostMapping("/contact-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addContact(@RequestBody Contact contact)
    {
        // Json:
        // {"id":1,"customerNumber":"12344123","subject":"defaultsubject","firstName":"Tim","lastName":"Boddenberg","email":"t@b.de","category":"defaultcategory","description":"defaultdescription"}

        contactRepository.save(contact);
        return new ResponseEntity<>("HTTP/1.1 201 Created",HttpStatus.CREATED);
    }

    @PutMapping("/contact-requests/{customerNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> modifyContact(@RequestBody Contact newContact, @PathVariable String customerNumber)
    {
        try{
            List<Contact> contactList = contactRepository.findByCustomerNumber(newContact.getCustomerNumber());

            if (contactList.isEmpty())
                throw new ContactNotFoundException("0");

            Contact contact = contactList.get(0);
            contact.setCustomerNumber(newContact.getCustomerNumber());
            contact.setFirstName(newContact.getFirstName());
            contact.setLastName(newContact.getLastName());
            contact.setCategory(newContact.getCategory());
            contact.setSubject(newContact.getSubject());
            contact.setDescription(newContact.getDescription());
            contact.setEmail(newContact.getEmail());

            contactRepository.save(contact);
            return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);

        } catch (UserNotFoundException exception)
        {
            return new ResponseEntity<>("HTTP/1.1 400 Bad Request - Contact Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/contact-requests/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable int id)
    {
        contactRepository.deleteById(id);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }

}
