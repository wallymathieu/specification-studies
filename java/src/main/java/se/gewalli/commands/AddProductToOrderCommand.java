package se.gewalli.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.gewalli.data.EntityNotFound;
import se.gewalli.data.Repository;

public final class AddProductToOrderCommand extends Command {
    public final int orderId;
    public final int productId;

    @JsonCreator
    public AddProductToOrderCommand(@JsonProperty("id") int id,
                                    @JsonProperty("version") int version,
                                    @JsonProperty("orderId") int orderId,
                                    @JsonProperty("productId") int productId) {
        super(id, version);
        this.orderId = orderId;
        this.productId = productId;
    }

    @Override
    public CommandType getType() {
        return CommandType.AddProductToOrderCommand;
    }

    @Override
    public void run(Repository repository) throws EntityNotFound {
        var order = repository.getOrder(orderId);
        if (!order.products.stream().anyMatch(p->p.id == productId)) {
            order.products.add(repository.getProduct(productId));
            order.version++;
            repository.save(order);
        }
    }
}
