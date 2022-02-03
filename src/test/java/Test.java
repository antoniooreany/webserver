import com.gorshkov.webserver.WebServer;

import java.io.IOException;

public class Test {

//    @Test
    public void test() throws IOException {
        WebServer server = new WebServer(3000);
//        server.setWebAppPath("src/main/resources" + url);
        server.start();
    }
}

