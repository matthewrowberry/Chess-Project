package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade comms;
    int porter;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);

        System.out.println("Started test HTTP server on " + port);
        comms = new ServerFacade(port);
    }

    @BeforeEach
    public void clear(){
        server.clear();

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void RegisterTest() {
        Assertions.assertEquals(comms.register("A","new","user"),"Logged in as A");
    }

    @Test
    public void RegisterFailTest(){
        Assertions.assertEquals(comms.register("A","new","user"),"Logged in as A");
        Assertions.assertEquals(comms.register("A","new","user"),"Error: already taken");
    }
}