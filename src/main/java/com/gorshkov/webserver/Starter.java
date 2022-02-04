package com.gorshkov.webserver;

import java.io.IOException;

public class Starter {
    public static void main(String[] args) throws IOException {
        WebServer server = new WebServer(3000);
        server.setWebAppPath("src/main/resources/");
        server.start();
    }
}
