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
import se.gewalli.commands.AddProductCommand;
import se.gewalli.commands.Command;
import se.gewalli.data.Repository;
import se.gewalli.entities.Product;

import java.util.concurrent.CompletableFuture;

@RestController()
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ProductsController {
    public static class CreateProduct{
        public int id;
        public float cost;
        public String name;
    }
    @Autowired
    private Repository repository;
    @Autowired
    private CommandsHandler commandsHandler;
    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.GET)
    public ResponseEntity<Product> get(@PathVariable int id) {
        return repository.tryGetProduct(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body((Product)null));
    }
    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public ResponseEntity<Product[]> get() {
        return ResponseEntity.ok(repository.getProducts().toArray(new Product[0]));
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Product.class)})
    @RequestMapping(value = "/api/products", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<Product>> add(@RequestBody()CreateProduct body) {
        Command command=new AddProductCommand(body.id,0, body.cost, body.name);
        return commandsHandler.handle(command).thenApply(result->
                result.fold(a -> ResponseEntity.ok(repository.tryGetProduct(body.id).orElse((Product)null)),
                        err->ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Product)null)));
    }
}
