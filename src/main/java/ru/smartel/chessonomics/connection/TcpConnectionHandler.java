package ru.smartel.chessonomics.connection;

import ru.smartel.chessonomics.dto.ConnectionContext;
import ru.smartel.chessonomics.dto.Player;
import ru.smartel.chessonomics.message.ErrorMessage;
import ru.smartel.chessonomics.message.handler.MessageHandlerRegistry;
import ru.smartel.chessonomics.message.parser.MessageParserRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TcpConnectionHandler extends Thread {
    private final MessageParserRegistry messageParserRegistry;
    private final MessageHandlerRegistry messageHandlerRegistry;
    private final Socket clientSocket;
    private final ConnectionContext connectionContext = new ConnectionContext();

    public TcpConnectionHandler(MessageParserRegistry messageParserRegistry,
                                MessageHandlerRegistry messageHandlerRegistry,
                                Socket socket) {
        this.messageParserRegistry = messageParserRegistry;
        this.messageHandlerRegistry = messageHandlerRegistry;
        this.clientSocket = socket;
    }

    @Override
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
                messageParserRegistry.parse(inputLine).ifPresentOrElse(
                        message -> messageHandlerRegistry.process(connectionContext, message),
                        () -> out.println(ErrorMessage.WRONG_COMMAND.toTcpString())
                );
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
