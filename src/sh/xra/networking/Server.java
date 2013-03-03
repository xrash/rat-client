package sh.xra.networking;

import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import android.util.Log;

public class Server
{
    private InetAddress address;
    private int port;

    public Server(InetAddress address, int port)
    {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress()
    {
        return this.address;
    }

    public String getHostAddress()
    {
        return this.address.getHostAddress();
    }

    public int getPort()
    {
        return this.port;
    }
}
