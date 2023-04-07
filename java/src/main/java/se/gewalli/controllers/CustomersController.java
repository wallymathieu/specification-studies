package se.gewalli.controllers;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import se.gewalli.CommandsHandler;
import se.gewalli.commands.AddCustomerCommand;
import se.gewalli.commands.Command;
import se.gewalli.data.Repository;
import se.gewalli.entities.Customer;

import java.util.concurrent.CompletableFuture;

@RestController()
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CustomersController {
    public static class CreateCustomer{
        public int id;
        public String firstname;
        public String lastname;
    }
    @Autowired
    private Repository repository;
    @Autowired
    private CommandsHandler commandsHandler;
    @RequestMapping(value = "/api/customers/{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> get(@PathVariable int id) {
        return repository.tryGetCustomer(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body((Customer)null));
    }
    @RequestMapping(value = "/api/customers", method = RequestMethod.GET)
    public ResponseEntity<Customer[]> get() {
        return ResponseEntity.ok(repository.getCustomers().toArray(new Customer[0]));
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Customer.class)})
    @RequestMapping(value = "/api/customers", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<Customer>> add(@RequestBody()CreateCustomer body) {
        Command command=new AddCustomerCommand(body.id,0, body.firstname, body.lastname);
        return commandsHandler.handle(command).thenApply(result->
                result.fold(a -> ResponseEntity.ok(repository.tryGetCustomer(body.id).orElse((Customer)null)),
                        err->ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Customer)null)));
    }
}
