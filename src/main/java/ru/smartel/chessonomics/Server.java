package ru.smartel.chessonomics;

import ru.smartel.chessonomics.connection.TcpConnectionHandler;
import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.message.handler.GiveMessageHandler;
import ru.smartel.chessonomics.message.handler.SearchMessageHandler;
import ru.smartel.chessonomics.message.handler.LoginMessageHandler;
import ru.smartel.chessonomics.message.handler.MessageHandlerRegistry;
import ru.smartel.chessonomics.message.handler.MoveMessageHandler;
import ru.smartel.chessonomics.message.handler.SpawnMessageHandler;
import ru.smartel.chessonomics.message.handler.StopMessageHandler;
import ru.smartel.chessonomics.message.parser.GiveMessageParser;
import ru.smartel.chessonomics.message.parser.SearchMessageParser;
import ru.smartel.chessonomics.message.parser.LoginMessageParser;
import ru.smartel.chessonomics.message.parser.MessageParserRegistry;
import ru.smartel.chessonomics.message.parser.MoveMessageParser;
import ru.smartel.chessonomics.message.parser.SpawnMessageParser;
import ru.smartel.chessonomics.message.parser.StopMessageParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    public static final int PORT = 1993;

    public static void main(String[] args) throws IOException {
        ConcurrentHashMap<String, ConnectionContext> searchingContextsByPlayerName = new ConcurrentHashMap<>();
        var messageParserRegistry = new MessageParserRegistry(
                new SearchMessageParser(),
                new StopMessageParser(),
                new LoginMessageParser(),
                new MoveMessageParser(),
                new SpawnMessageParser(),
                new GiveMessageParser()
        );
        var messageHandlerRegistry = new MessageHandlerRegistry(
                new LoginMessageHandler(),
                new SearchMessageHandler(searchingContextsByPlayerName),
                new StopMessageHandler(searchingContextsByPlayerName),
                new MoveMessageHandler(),
                new SpawnMessageHandler(),
                new GiveMessageHandler()
        );

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is waiting for connections");
            while (true) {
                new TcpConnectionHandler(messageParserRegistry, messageHandlerRegistry,
                        searchingContextsByPlayerName, serverSocket.accept()).start();
            }
        }
    }
}
