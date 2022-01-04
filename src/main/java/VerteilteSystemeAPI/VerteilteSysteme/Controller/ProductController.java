package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Product;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.ProductNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.UserNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.ProductRepository;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController{

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/products", produces = "application/json")
    @ApiOperation("Gibt alle Produkte aus dem Produkt Repository wieder, ohne diese zu filtern. Rückgabewert ist eine Liste als Json String formatiert.")
    public String getAllProducts()
    {
        List<Product> productList = new ArrayList<>();

        if (productRepository != null)
            productList = productRepository.findAll();

        return new Gson().toJson(productList);
    }

    @GetMapping(value = "/products/{id}",produces = "application/json")
    @ApiOperation("Gibt ein Produkt anhand der gegebenen Id zurück. Da die Id unique ist, wird hier nur ein Objekt als Json String zurückgegeben.")
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

    @GetMapping(value = "/products/byname/{name}", produces = "application/json")
    @ApiOperation("Gibt ein Produkt anhand seines Namens als Json String zurück.")
    public String getSpecificProductByName(@PathVariable String name)
    {
        try {
            List<Product> productList = productRepository.findByName(name);

            if (productList.isEmpty())
                throw new ProductNotFoundException(0);

            return new Gson().toJson(productList.get(0));
        } catch (ProductNotFoundException exception)
        {
            return "Product Not Found";
        }
    }

    @PostMapping("/products")
    @ApiOperation("Fügt ein Produkt hinzu. Wenn die Operation erfolgreich war, wird der Http-Statuscode 200 zurückgegeben.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addProduct(@RequestBody Product product)
    {
        // Json:
        // {"id":1,"name":"Computer","price":"999.00","brand":"Dell","since":"2021","url":"/dellcomputer"}

        productRepository.save(product);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }

    @PutMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Ändert ein bestehendes Produkt ab. Wenn die Operation erfolgreich war, wird der Http-Statuscode 200 zurückgegeben, sonst 400.")
    public ResponseEntity<String> modifyProduct(@RequestBody Product newProduct)
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

            return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
        } catch (UserNotFoundException exception)
        {
            return new ResponseEntity<>("HTTP/1.1 400 Bad Request - User Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/products/{id}")
    @ApiOperation("Löscht ein Produkt anhand der Id. Wenn die Operation erfolgreich ist, wird der Http-Statuscode 200 zurückgegeben.")
    public ResponseEntity<String> deleteProduct(@PathVariable int id)
    {
        productRepository.deleteById(id);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }

}
