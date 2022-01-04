package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Orders;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.OrderNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.OrderRepository;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController{

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/orders")
    @ApiOperation("Gibt alle Bestellungen zurück, ohne diese zu filtern. Rückgabewert ist eine Liste als Json String formatiert.")
    public String getAllOrders()
    {
        return new Gson().toJson(orderRepository.findAll());
    }

    @GetMapping("/orders/{product}")
    @ApiOperation("Gibt eine Bestellung anhand eines Produktes zurück. Rückgabewert ist ein Bestellobjekt als Json String formatiert.")
    public String getOrderByProduct(@PathVariable String product)
    {
        try {
            List<Orders> orderList = orderRepository.findByProduct(product);

            if (orderList.isEmpty())
                throw new OrderNotFoundException();

            return new Gson().toJson(orderList.get(0));

        } catch (OrderNotFoundException exception)
        {
            return "Order Not Found";
        }
    }

    @PostMapping("/orders")
    @ApiOperation("Fügt eine Bestellung hinzu. Wenn die Operation erfolgreich ist, wird der Http-Statuscode 200 zurückgegeben.")
    public ResponseEntity<String> addOrder(@RequestBody Orders order)
    {
        orderRepository.save(order);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }

    @PutMapping("/orders/{id}")
    @ApiOperation("Ändert eine bestehende Bestellung ab. wenn die Operation erfolgreich ist, wird der Http-Statuscode 200 zurückgegeben, sonst 400.")
    public ResponseEntity<String> replaceOrder(@RequestBody Orders newOrder, @PathVariable int id)
    {
        try{
            orderRepository.findById(id).map(order -> {
                order.setId(newOrder.getId());
                order.setProduct(newOrder.getProduct());
                order.setPurchaser(newOrder.getPurchaser());
                order.setQuantity(newOrder.getQuantity());
                order.setTotalCosts(newOrder.getTotalCosts());

                orderRepository.save(order);
                return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
            }).orElseThrow(OrderNotFoundException::new);
            return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
        } catch (OrderNotFoundException exception)
        {
            return new ResponseEntity<>("HTTP/1.1 400 Bad Request - User Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/orders/{id}")
    @ApiOperation("Löscht eine Bestellung anhand der gegebenen Id. Ist die Operation erfolgreich, wird der Http-Statuscode 200 zurückgegeben.")
    public ResponseEntity<String> deleteOrder(@PathVariable int id)
    {
        orderRepository.deleteById(id);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }


}
