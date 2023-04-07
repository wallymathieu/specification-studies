package se.gewalli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.gewalli.commands.Command;
import se.gewalli.data.EntityNotFound;
import se.gewalli.data.Repository;
import se.gewalli.kyminon.Result;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CommandsHandler {
    @Autowired
    private AppendBatch appendBatch;
    @Autowired
    private Repository repository;
    Logger logger = LoggerFactory.getLogger(CommandsHandler.class);

    public CompletableFuture<Result<Integer, FailureReason>> handle(Command c) {
        ArrayList<Command> l = new ArrayList<>();
        l.add(c);
        try {
            c.run(repository);
        } catch (EntityNotFound entityNotFound) {
            logger.error("entity not found", entityNotFound);
        }
        return appendBatch.batch(l);
    }
}
