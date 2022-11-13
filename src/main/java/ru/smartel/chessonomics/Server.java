package ru.smartel.chessonomics;

import ru.smartel.chessonomics.command.CommandHandler;
import ru.smartel.chessonomics.command.FindCommandHandler;
import ru.smartel.chessonomics.command.LoginCommandHandler;
import ru.smartel.chessonomics.command.MoveCommandHandler;
import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final List<CommandHandler> commandHandlers;

    public Server() {
        ConcurrentHashMap<String, ConnectionContext> searchingContextsByPlayerName = new ConcurrentHashMap<>();
        commandHandlers = List.of(
                new LoginCommandHandler(),
                new FindCommandHandler(searchingContextsByPlayerName),
                new MoveCommandHandler()
        );
    }

    public static void main(String[] args) throws IOException {
        new Server().start(1993);
    }

    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is waiting for connections");
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
                System.out.println("Client connected");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                connectionContext.setOutputWriter(out);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    var playerName = Optional.ofNullable(connectionContext.getPlayer())
                            .map(Player::getName)
                            .orElse("guest");
                    System.out.println("Received command from " + playerName + ": " + inputLine);
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
                                    () -> out.println("error WrongCommand"));
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
