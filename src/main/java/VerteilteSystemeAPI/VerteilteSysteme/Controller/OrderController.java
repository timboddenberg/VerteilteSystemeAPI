package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Orders;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.OrderNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.OrderRepository;
import com.google.gson.Gson;
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
    public String getAllOrders()
    {
        return new Gson().toJson(orderRepository.findAll());
    }

    @GetMapping("/orders/{product}")
    public String getOrderById(@PathVariable String product)
    {
        try {
            List<Orders> orderList = orderRepository.findByProduct(product);

            if (orderList.isEmpty())
                throw new OrderNotFoundException();
            else
                new Gson().toJson(orderList);

        } catch (OrderNotFoundException exception)
        {
            return "Order Not Found";
        }

        return "Something went wrong.";
    }

    @PostMapping("/orders")
    public ResponseEntity<String> addOrder(@RequestBody Orders order)
    {
        orderRepository.save(order);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }

    @PutMapping("/orders/{id}")
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
    public ResponseEntity<String> deleteOrder(int id)
    {
        orderRepository.deleteById(id);
        return new ResponseEntity<>("HTTP/1.1 200 OK", HttpStatus.OK);
    }


}
