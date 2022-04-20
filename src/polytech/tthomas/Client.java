package polytech.tthomas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;


public class Client {
    private static final String REMOTE_ADDRESS = "localhost";
    private static final InetSocketAddress MAIN_SERVER_ADDRESS = new InetSocketAddress(REMOTE_ADDRESS, Server.SERVER_PORT);
    private static InetSocketAddress CHATROOM_SERVER_ADDRESS;

    private static final Scanner _scanner = new Scanner(System.in);
    private static DatagramSocket socket = null;
    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        chooseNicknameAndContactServer();
        chooseAvailableChatroom();
        DatagramPacket dp = new DatagramPacket(new byte[Server.BUFFER_SIZE], Server.BUFFER_SIZE);
        socket.receive(dp);
        System.out.println(new String(dp.getData()).trim());
        CHATROOM_SERVER_ADDRESS = new InetSocketAddress(REMOTE_ADDRESS, dp.getPort());

        new Thread(() -> {
            while (true) {
                final String message = _scanner.nextLine().trim();
                final DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), CHATROOM_SERVER_ADDRESS);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                final DatagramPacket packet = new DatagramPacket(new byte[Server.BUFFER_SIZE], Server.BUFFER_SIZE);
                try {
                    socket.receive(packet);
                    System.out.println(new String(packet.getData()).trim());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void chooseAvailableChatroom() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[Server.BUFFER_SIZE], Server.BUFFER_SIZE);
        socket.receive(packet);
        System.out.println(new String(packet.getData()).trim());
        final String action = _scanner.next().trim();
        packet = new DatagramPacket(action.getBytes(), action.length(), MAIN_SERVER_ADDRESS);
        socket.send(packet);
    }

    private static void chooseNicknameAndContactServer() throws IOException {
        System.out.print("Choose your nickname:\t");
        final String nickname = _scanner.next().trim();
        DatagramPacket packet = new DatagramPacket(nickname.getBytes(), nickname.length(), MAIN_SERVER_ADDRESS);
        socket.send(packet);
    }
}
