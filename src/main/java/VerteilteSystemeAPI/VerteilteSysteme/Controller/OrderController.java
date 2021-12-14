package VerteilteSystemeAPI.VerteilteSysteme.Controller;

import VerteilteSystemeAPI.VerteilteSysteme.Entities.Orders;
import VerteilteSystemeAPI.VerteilteSysteme.Exceptions.OrderNotFoundException;
import VerteilteSystemeAPI.VerteilteSysteme.Repositories.OrderRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

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
    public String addOrder(@RequestBody Orders order)
    {
        return new Gson().toJson(orderRepository.save(order));
    }

    @PutMapping("/orders/{id}")
    public String replaceOrder(@RequestBody Orders newOrder, @PathVariable int id)
    {
        try{
            orderRepository.findById(id).map(order -> {
                order.setId(newOrder.getId());
                order.setProduct(newOrder.getProduct());
                order.setPurchaser(newOrder.getPurchaser());
                order.setQuantity(newOrder.getQuantity());
                order.setTotalCosts(newOrder.getTotalCosts());

                return new Gson().toJson(orderRepository.save(order));
            });
        } catch (OrderNotFoundException exception)
        {
            return "Order Not Found.";
        }

        return "Something went wrong.";
    }

    @DeleteMapping("/orders/{id}")
    public String deleteOrder(int id)
    {
        orderRepository.deleteById(id);
        return "Löschen erfolgreich.";
    }


}
