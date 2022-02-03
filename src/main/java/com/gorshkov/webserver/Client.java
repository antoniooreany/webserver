package com.gorshkov.webserver;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String URI = "localhost";
    private static final int PORT = 3000;

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(URI, PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             Scanner scanner = new Scanner(System.in);
        ) {
            while (true) {
                String nextLine = scanner.nextLine();
                writer.write(nextLine + "\r\n");
                writer.flush();
                String contentFromServer = reader.readLine();
                System.out.println(contentFromServer);
                writer.write(contentFromServer);
//                scanner.close();
            }
        }
    }
}
