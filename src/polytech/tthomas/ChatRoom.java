package polytech.tthomas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ChatRoom implements Runnable {
    private final ArrayList<Chatter> _clients = new ArrayList<>();
    private final DatagramSocket _socket;
    private final String _author;
    public final int roomNumber;

    public ChatRoom(Chatter client) throws IOException {
        _socket = new DatagramSocket();
        roomNumber = _socket.getLocalPort();
        _author = "Chatroom #" + roomNumber;
        addClient(client);
    }

    public void addClient(Chatter client) throws IOException {
        _clients.add(client);
        welcomeClient(client);
    }

    private void welcomeClient(Chatter client) throws IOException {
        final String welcomeMessage = MessageFormatter.format(_author, "Hello " + client.nickname + "!");
        final DatagramPacket packet = new DatagramPacket(welcomeMessage.getBytes(), welcomeMessage.length(), client.address, client.port);
        _socket.send(packet);
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[Server.BUFFER_SIZE], Server.BUFFER_SIZE);
                _socket.receive(packet);
                final InetAddress authorAddress = packet.getAddress();
                final int authorPort = packet.getPort();
                Chatter author = _clients.stream().filter(x -> x.address.equals(authorAddress) && x.port == authorPort).findFirst().get();
                final String message = MessageFormatter.format(author.nickname + "(" + author.address + ":" + authorPort + ")", new String(packet.getData()).trim());
                for (Chatter client : _clients) {
                    packet = new DatagramPacket(message.getBytes(), message.length(), client.address, client.port);
                    _socket.send(packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
