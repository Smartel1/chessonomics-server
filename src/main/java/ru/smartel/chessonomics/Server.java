package ru.smartel.chessonomics;

import ru.smartel.chessonomics.connection.TcpConnectionHandler;
import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.message.handler.FindMessageHandler;
import ru.smartel.chessonomics.message.handler.LoginMessageHandler;
import ru.smartel.chessonomics.message.handler.MessageHandlerRegistry;
import ru.smartel.chessonomics.message.handler.MoveMessageHandler;
import ru.smartel.chessonomics.message.handler.SpawnMessageHandler;
import ru.smartel.chessonomics.message.parser.FindMessageParser;
import ru.smartel.chessonomics.message.parser.LoginMessageParser;
import ru.smartel.chessonomics.message.parser.MessageParserRegistry;
import ru.smartel.chessonomics.message.parser.MoveMessageParser;
import ru.smartel.chessonomics.message.parser.SpawnMessageParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    public static final int PORT = 1993;

    public static void main(String[] args) throws IOException {
        ConcurrentHashMap<String, ConnectionContext> searchingContextsByPlayerName = new ConcurrentHashMap<>();
        var messageParserRegistry = new MessageParserRegistry(
                new FindMessageParser(),
                new LoginMessageParser(),
                new MoveMessageParser(),
                new SpawnMessageParser()
        );
        var messageHandlerRegistry = new MessageHandlerRegistry(
                new LoginMessageHandler(),
                new FindMessageHandler(searchingContextsByPlayerName),
                new MoveMessageHandler(),
                new SpawnMessageHandler()
        );

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is waiting for connections");
            while (true) {
                new TcpConnectionHandler(messageParserRegistry, messageHandlerRegistry, serverSocket.accept()).start();
            }
        }
    }
}
