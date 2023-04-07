package se.gewalli.data;

import se.gewalli.entities.Customer;
import se.gewalli.entities.Order;
import se.gewalli.entities.Product;

import java.util.Collection;
import java.util.Optional;

public abstract class Repository {
    public abstract Optional<Customer> tryGetCustomer(int customerId);

    public abstract Optional<Product> tryGetProduct(int productId);

    public abstract Optional<Order> tryGetOrder(int orderId);
    public abstract Optional<Order> tryGetOrderWithCustomerLastname(String lastname);

    public abstract void save(Product product);

    public abstract void save(Order order);

    public abstract void save(Customer customer);
    public Customer getCustomer(int customerId) throws EntityNotFound {
        return tryGetCustomer(customerId)
                .orElseThrow(()->new EntityNotFound(String.format("Could not find customer %d", customerId)));
    }
    public Product getProduct(int productId) throws EntityNotFound {
        return tryGetProduct(productId)
                .orElseThrow(()->new EntityNotFound(String.format("Could not find product %d", productId)));
    }
    public Order getOrder(int orderId) throws EntityNotFound {
        return tryGetOrder(orderId)
                .orElseThrow(()->new EntityNotFound(String.format("Could not find order %d", orderId)));
    }

    public abstract Collection<Customer> getCustomers();
    public abstract Collection<Product> getProducts();
    public abstract Collection<Order> getOrders();
}
