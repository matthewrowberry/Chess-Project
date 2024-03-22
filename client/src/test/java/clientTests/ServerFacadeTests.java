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

    @Test
    public void LoginTest() {
        comms.register("A","new","user");
        Assertions.assertEquals(comms.login("A","new"),"Logged in as A");
    }

    @Test
    public void LoginFailTest(){
        comms.register("A","new","user");
        Assertions.assertEquals("Error: unauthorized",comms.login("A","notpassword"));

    }

    @Test
    public void LogoutTest(){
        comms.register("A","new","user");
        Assertions.assertEquals("Error: unauthorized",comms.login("A","notpassword"));
        Assertions.assertEquals("Logged out",comms.logout());

    }

    @Test
    public void LogoutFailTest(){
        comms.register("A","new","user");

        server.clear();
        Assertions.assertEquals("Error: unauthorized",comms.logout());

    }

    @Test
    public void createTest(){
        comms.register("A","new","user");
        Assertions.assertEquals("Error: unauthorized",comms.login("A","notpassword"));
        Assertions.assertEquals("Game with name of \"scooby doo\" created", comms.createGame("scooby doo"));

    }

    @Test
    public void createFailTest(){
        comms.register("A","new","user");
        Assertions.assertEquals("Error: unauthorized",comms.login("A","notpassword"));
        Assertions.assertEquals("Game name cannot be null", comms.createGame(null));

    }


    @Test
    public void listTest(){
        comms.register("A","new","user");
        Assertions.assertEquals("Error: unauthorized",comms.login("A","notpassword"));
        Assertions.assertEquals("Game with name of \"scooby doo\" created", comms.createGame("scooby doo"));
        Assertions.assertEquals("1) scooby doo\n\tWhite team: null\n\tBlack team: null\n",comms.listGames());

    }

    @Test
    public void listFailTest(){
        comms.register("A","new","user");
        Assertions.assertEquals("Error: unauthorized",comms.login("A","notpassword"));
        comms.createGame("Velma");
        Assertions.assertNotEquals("1) scooby doo\n\tWhite team: null\n\tBlack team: null\n",comms.listGames());

    }



}