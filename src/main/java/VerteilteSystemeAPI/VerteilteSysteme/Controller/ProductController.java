package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Product;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.ProductNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.UserNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.ProductRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String getAllProducts()
    {
        // curl GET http://localhost:8080/products
        List<Product> productList = new ArrayList<>();

        if (productRepository != null)
            productList = productRepository.findAll();

        return new Gson().toJson(productList);
    }

    @GetMapping("/products/{id}")
    public String getSpecificProduct(@PathVariable int id)
    {
        try{
            Product specificProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
            return new Gson().toJson(specificProduct);
        }
        catch (ProductNotFoundException exception)
        {
            return "User Not Found";
        }
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product)
    {
        // curl -H "Content-Type: application/json" -X POST http://localhost/user/add -d "{\"id\":\"1\",\"UserName\":\"Tim\", \"FirstName\":\"Tim\",\"LastName\":\"Boddenberg\",\"Email\":\"t@b.de\",\"Password\":\"12345\"}"
        // {"UserName":"timboddenberg","FirstName":"Tim","LastName":"Boddenberg","Email":"t@b.de","Password":"12345"}

        return productRepository.save(product);
    }

    @PutMapping("/products/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public String modifyProduct(@RequestBody Product newProduct, @PathVariable int id)
    {
        try{
            productRepository.findById(newProduct.getId()).map(product-> {
                product.setName(newProduct.getName());
                product.setPrice(newProduct.getPrice());
                product.setBrand(newProduct.getBrand());
                product.setSince(newProduct.getSince());
                product.setUrl(newProduct.getUrl());
                return new Gson().toJson(productRepository.save(product));
            }).orElseThrow(() -> new ProductNotFoundException(newProduct.getId()));
        } catch (UserNotFoundException exception)
        {
            return "User not Found";
        }

        return "Something went wrong.";
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable int id)
    {
        productRepository.deleteById(id);
    }

}
