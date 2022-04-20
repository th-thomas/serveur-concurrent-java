package polytech.tthomas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


public class Server {
    public static final int SERVER_PORT = 200;
    public static final int BUFFER_SIZE = 512;

    private static final ArrayList<ChatRoom> _chatRooms = new ArrayList<>();
    private static final String _author = "Main chat server";

    public static void main(String[] args) throws IOException {
        final DatagramSocket ds = new DatagramSocket(SERVER_PORT);
        DatagramPacket dp = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        while (true) {
            ds.receive(dp);
            final Chatter client = new Chatter(dp.getAddress(), dp.getPort(), new String(dp.getData()).trim());

            String message;
            if (_chatRooms.size() == 0) {
                message = MessageFormatter.format(_author, "No available chatroom. You can (c)reate a chatroom.");
            } else {
                String chatroomNumbers = _chatRooms.stream().map(x -> String.valueOf(x.roomNumber)).collect(Collectors.joining(","));
                message = MessageFormatter.format(_author, "Available chatroom numbers are: " + chatroomNumbers + ". You can also (c)reate a chatroom.");
            }
            dp = new DatagramPacket(message.getBytes(), message.length(), client.address, client.port);
            ds.send(dp);

            dp = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            ds.receive(dp);
            String clientAction = new String(dp.getData()).trim();
            if (clientAction.equals("c")) {
                final ChatRoom room = new ChatRoom(client);
                _chatRooms.add(room);
                new Thread(room).start();
            } else if (_chatRooms.size() != 0) {
                Optional<ChatRoom> selectedChatroom = _chatRooms.stream().filter(x -> String.valueOf(x.roomNumber).equals(clientAction)).findFirst();
                if (selectedChatroom.isPresent()) {
                    selectedChatroom.get().addClient(client);
                }
            }
        }
    }
}
