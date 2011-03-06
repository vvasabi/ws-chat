package ca.wasabistudio.chat.connector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestConnector {

    private ClassPathXmlApplicationContext context;
    private Connector connector;
    private EntityManagerFactory emf;
    private EntityManager em;
    private String ip = "173.34.56.54";
    private String sessionId = "d41e1d4abf776069eddd90b2d7eac6ae";

    @BeforeMethod
    public void setup() {
        String[] paths = new String[] {
                "META-INF/spring-jpa.xml",
                "META-INF/connector.xml",
                "META-INF/services.xml"
        };

        context = new ClassPathXmlApplicationContext(paths);
        connector = context.getBean(Connector.class);

        String unit = "connector";
        emf = Persistence.createEntityManagerFactory(unit);
        em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User("wasabi");
        em.persist(user);
        em.flush();

        Session session = new Session(sessionId);
        session.setIp(ip);
        session.setUserId(user.getId());
        em.persist(session);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    @AfterMethod
    public void tearDown() {
        context.close();
    }

    @Test
    public void testValidateSessionTrue() {
        assertTrue(connector.validateSession(sessionId, ip));
    }

    @Test
    public void testValidateSessionFalse() {
        assertTrue(connector.validateSession(sessionId, "123.321.45.67"));
    }

    @Test
    public void testGetUsername() {
        assertTrue(connector.getUsername(sessionId).equals("wasabi"));
    }

    @Test
    public void testGetUsernameNull() {
        assertNull(connector.getUsername("dfasdfasdfas"));
    }

    @Test
    public void testRefreshSessionSuccessful() {
        assertTrue(connector.refreshSession(sessionId));
    }

    @Test
    public void testRefreshSessionFailure() {
        assertFalse(connector.refreshSession("dfsajkflasdfjklsad"));
    }

}
