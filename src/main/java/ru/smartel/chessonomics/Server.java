package ru.smartel.chessonomics;

import ru.smartel.chessonomics.command.CommandHandler;
import ru.smartel.chessonomics.command.FindCommandHandler;
import ru.smartel.chessonomics.command.LoginCommandHandler;
import ru.smartel.chessonomics.dto.ConnectionContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final ConcurrentHashMap<String, ConnectionContext> searchingContextsByPlayerName = new ConcurrentHashMap<>();
    private final List<CommandHandler> commandHandlers;

    public Server() {
        commandHandlers = List.of(
                new LoginCommandHandler(),
                new FindCommandHandler(searchingContextsByPlayerName)
        );
    }

    public static void main(String[] args) throws IOException {
        new Server().start(1993);
    }

    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("server is waiting for connections");
            while (true) {
                new ChessonomicsHandler(serverSocket.accept()).start();
            }
        }
    }

    private class ChessonomicsHandler extends Thread {
        private final Socket clientSocket;
        private final ConnectionContext connectionContext = new ConnectionContext();

        public ChessonomicsHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                connectionContext.setOutputWriter(out);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }

                    String finalInputLine = inputLine;
                    commandHandlers.stream()
                            .filter(h -> h.accepts(finalInputLine))
                            .findFirst()
                            .ifPresentOrElse(
                                    h -> h.process(connectionContext, finalInputLine),
                                    () -> out.println("wrong command"));
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                System.out.println("shit happens");
            }
        }
    }
}
