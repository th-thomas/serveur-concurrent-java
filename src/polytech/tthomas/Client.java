package polytech.tthomas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;


public class Client {
    private static final String SERVER_ADDRESS = "localhost";

    public static void main(String[] args) throws IOException {
        final DatagramSocket ds = new DatagramSocket();
        DatagramPacket dp = new DatagramPacket(new byte[0], 0, new InetSocketAddress(SERVER_ADDRESS, Serveur.SERVER_PORT));
        ds.send(dp);

        dp = new DatagramPacket(new byte[Serveur.BUFFER_SIZE], Serveur.BUFFER_SIZE);
        ds.receive(dp);

        MessageLogger.LogMessage(String.format("%s:%s", dp.getAddress(), dp.getPort()), new String(dp.getData()).trim());

        while (true) {
            final Scanner scanner = new Scanner(System.in);
            final String message = scanner.nextLine().trim();
            dp = new DatagramPacket(message.getBytes(), message.length(), new InetSocketAddress(SERVER_ADDRESS, dp.getPort()));
            ds.send(dp);
        }
    }
}
