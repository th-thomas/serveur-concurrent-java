package polytech.tthomas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Communication implements Runnable {
    private final InetAddress _clientAddress;
    private final int _clientPort;

    public Communication(InetAddress clientAddress, int clientPort) {
        _clientAddress = clientAddress;
        _clientPort = clientPort;
    }

    @Override
    public void run() {
        try (DatagramSocket ds = new DatagramSocket()) {
            final String hello = "Hello!";
            final DatagramPacket dp = new DatagramPacket(hello.getBytes(), hello.length(), _clientAddress, _clientPort);
            ds.send(dp);
            while (true) {
                dp.setData(new byte[Serveur.BUFFER_SIZE]);
                ds.receive(dp);
                MessageLogger.LogMessage(String.format("%s:%s", dp.getAddress(), dp.getPort()), new String(dp.getData()).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
