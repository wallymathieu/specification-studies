import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import se.gewalli.commands.Command;
import se.gewalli.data.EntityNotFound;
import se.gewalli.data.HibernateRepository;
import se.gewalli.data.Repository;
import xmlimport.GetCommands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Supplier;

public class CustomerDataTests {
    private Supplier<SessionFactory> sessionFactory = () -> setUp();

    private static SessionFactory setUp() {
        var sessionFactory = new Configuration().configure().buildSessionFactory();
        var getCommands=new GetCommands();

        for (Command command : getCommands.Get()) {
            runInSession(sessionFactory, command);
        }
        return sessionFactory;
    }
    private static void runInSession(SessionFactory sessionFactory, Command command){
        Transaction transaction=null;
        try (var session= sessionFactory.openSession()){
            transaction = session.beginTransaction();
            var repository = new HibernateRepository(session);

            command.run(repository);
            transaction.commit();
        }catch (Exception e){
            if (transaction!=null)transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    Repository repository;
    Session _session;
    @BeforeEach
    public void beforeEach() {
        _session=sessionFactory.get().openSession();
        repository=new HibernateRepository(_session);
    }
    @AfterEach
    public void afterEach(){
        _session.close();
    }
    @Test
    public void canGetCustomerById() throws EntityNotFound {
        assertEquals("Steve" , repository.getCustomer(1).firstname);
    }
    @Test
    public void canGetOrderThroughQuery() throws EntityNotFound {
        assertEquals("Bohlen", repository.tryGetOrderWithCustomerLastname("Bohlen").get().customer.lastname);
    }
    @Test
    public void canGetProductById() throws EntityNotFound {
        assertEquals("Yo-yo" , repository.getProduct(1).name);
    }
    @Test
    public void orderContainsProduct() throws EntityNotFound {
        assertEquals(3 , repository.getOrder(1).products.size());
    }
}
