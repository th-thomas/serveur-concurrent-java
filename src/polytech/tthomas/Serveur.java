package polytech.tthomas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Serveur {
    public static final int SERVER_PORT = 200;
    public static final int BUFFER_SIZE = 512;

    public static void main(String[] args) throws IOException {
        final DatagramSocket ds = new DatagramSocket(SERVER_PORT);
        final DatagramPacket dp = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        while (true) {
            ds.receive(dp);
            Thread thread = new Thread(new Communication(dp.getAddress(), dp.getPort()));
            thread.start();
        }
    }
}
