package com.gorshkov.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WebServer {

    private final int port;
    private String webAppPath = "src/main/resources/";

    public WebServer(int port) {
        this.port = port;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
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
                writeHeader(bufferedWriter);
                System.out.println("Writing content");
                writeBody(bufferedWriter, resource);
            }
            System.out.println("++++++++++++++++++++++++++++++++++++++++++");

            String line = "";
            String header = "";
            String resourceName;
            try (Socket socket = serverSocket.accept();
                 BufferedReader bufferedReader = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 BufferedWriter bufferedWriter = new BufferedWriter(
                         new OutputStreamWriter(socket.getOutputStream()))) {
                while ((line = bufferedReader.readLine()) != null || !(line = bufferedReader.readLine()).isEmpty()); {
                    if (line.startsWith("GET")) {
                        header = line;
                    }
                    resourceName = getResource(header);
                }
                writeBody(bufferedWriter, resourceName);
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

    private void writeBody(BufferedWriter bufferedWriter, String resourceName) throws IOException {
        StringBuilder content = new StringBuilder();
        Scanner scanner = new Scanner(new File(webAppPath + resourceName));
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine());
        }
        bufferedWriter.write(content.toString());
    }

    private void writeHeader(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("HTTP/1.1 200 OK");
        bufferedWriter.newLine();
        bufferedWriter.newLine();
    }
}