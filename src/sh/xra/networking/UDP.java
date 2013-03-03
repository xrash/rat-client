package sh.xra.networking;

import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import android.util.Log;

public class UDP
{
    private static Server server;

    public static void configure(String host, int port)
    {
        try {
            InetAddress address = InetAddress.getByName(host);
            UDP.server = new Server(address, port);
        } catch (Exception e) {
            Log.e("UDP", "configure" + e);
        }
    }

    public static boolean isConfigured()
    {
        return (UDP.server instanceof Server);
    }

    public static Server getServer()
    {
        return UDP.server;
    }

    public static void send(byte[] data)
    {
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(data, data.length, UDP.server.getAddress(), UDP.server.getPort());

            ///* debug
            String d = "";
            for (byte b : data) {
                d += String.valueOf(b) + "\t";
            }
            Log.d("Rat", d);
            //*/

            socket.send(packet);
        } catch (Exception e) {
            Log.e("UDP", "send" + e);
        }
    }

    public static boolean knock(InetAddress address)
    {
        byte[] data = { (byte)0xFF };
        byte[] recv = { (byte)0x00 };

        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, 4500);
            DatagramPacket p = new DatagramPacket(recv, recv.length, address, 4500);
            socket.setSoTimeout(200);
            socket.send(packet);
            socket.receive(p);

            return Arrays.equals(p.getData(), data);
        } catch (SocketTimeoutException e) {
            Log.d("Rat", address.getHostAddress() + "...");
        } catch (Exception e) {
            Log.e("UDP", "knock:" + e);
        }
        return false;
    }
}
