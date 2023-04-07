package se.gewalli.entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.time.Instant;

@Entity(name="Orders")
public class Order {
    @javax.persistence.Id
    public int id;
    @OneToOne
    public Customer customer;
    public Instant orderDate;

    @OneToMany
    public Collection<Product> products=new ArrayList<>();
    public int version;

    public Order(int id, Customer customer, Instant orderDate, Collection<Product> products, int version) {
        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
        this.products = Collections.unmodifiableCollection(products);
        this.version = version;
    }

    protected Order() {
    }
}
