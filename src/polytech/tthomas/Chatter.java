package polytech.tthomas;

import java.net.InetAddress;

public class Chatter {
    public InetAddress address;
    public int port;
    public String nickname;

    public Chatter(InetAddress address, int port, String nickname) {
        this.address = address;
        this.port = port;
        this.nickname = nickname;
    }
}
