package com.gorshkov.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WebServer {

    private final int port;

    public WebServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        new WebServer(3000).start();
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Started");
            try (Socket socket = serverSocket.accept();
                 BufferedReader bufferedReader = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 BufferedWriter bufferedWriter = new BufferedWriter(
                         new OutputStreamWriter(socket.getOutputStream()))) {
                String header = getHeader(bufferedReader);
                String resource = getResource(header);
                System.out.println("Writing response");
                writeResponse(bufferedWriter);
                System.out.println("Writing content");
                writeContent(bufferedWriter, resource);
            }

            String line = "";
            String header = "";
            String resource = "";
            try (Socket socket = serverSocket.accept();
                 BufferedReader bufferedReader1 = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 BufferedWriter bufferedWriter = new BufferedWriter(
                         new OutputStreamWriter(socket.getOutputStream()))) {
                do {
                    if (line.startsWith("GET")) {
                        header = line;
                    }
                    resource = header
                            .replaceAll("GET /", "")
                            .replaceAll(" HTTP.*", "");
                } while ((line = bufferedReader1.readLine()) != null
                        || !(line = bufferedReader1.readLine()).isEmpty());
                writeContent(bufferedWriter, resource);
                System.out.println("++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("Finish");
            }
        }
    }

    private String getHeader(BufferedReader bufferedReader) throws IOException {
        String line;
        String header = "";
        while (!(line = bufferedReader.readLine()).isEmpty()) {
            System.out.println(line);
            if (line.startsWith("GET")) {
                header = line;
            }
        }
        return header;
    }

    private String getResource(String header) {
        String resource = header.replaceAll("GET /", "")
                .replaceAll(" HTTP.*", "");
        System.out.println(resource);
        return resource;
    }

    private void writeContent(BufferedWriter bufferedWriter, String resource) throws IOException {
        StringBuilder content = new StringBuilder();
        Scanner scanner = new Scanner(new File("src/main/resources/" + resource));
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine());
        }
        bufferedWriter.write(content.toString());
    }

    private void writeResponse(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("HTTP/1.1 200 OK");
        bufferedWriter.newLine();
        bufferedWriter.newLine();
    }
}