package com.gorshkov.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WebServer {

    private final int port;
    private String webAppPath;

    public WebServer(int port) {
        this.port = port;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Started");
            writeResponse(serverSocket);
            writeResponse(serverSocket);
            System.out.println("Finished");
        }
    }

    private void writeResponse(ServerSocket serverSocket) throws IOException {
        try (Socket socket = serverSocket.accept(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            String resource = getResource(bufferedReader);
            writeResponse(bufferedWriter, resource);
        }
    }

    private void writeResponse(BufferedWriter bufferedWriter, String resource) throws IOException {
        System.out.println("Writing response");
        writeHeader(bufferedWriter);
        System.out.println("Writing content");
        writeBody(bufferedWriter, resource);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
    }

    private String getResource(BufferedReader bufferedReader) throws IOException {
        String header = getHeader(bufferedReader);
        return getResource(header);
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
        String[] headerParts = header.split("\s");
        String httpMethod = headerParts[0];
        String resource = headerParts[1];
        String httpVersion = headerParts[2];
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