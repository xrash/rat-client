package sh.xra.networking;

import java.util.ArrayList;
import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.NetworkInterface;
import java.net.InterfaceAddress;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Arrays;
import java.math.BigInteger;
import android.util.Log;
import android.widget.ArrayAdapter;

public class Scanner
{
    public ArrayList<InetAddress> scan()
    {
        ArrayList<InetAddress> hosts = new ArrayList();

        try {
            Enumeration<NetworkInterface> networkInterfaces
                = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                for (InterfaceAddress interfaceAddress
                         : networkInterface.getInterfaceAddresses()) {
                    int addressLength = interfaceAddress.getAddress().getAddress().length;
                    short prefixLength = interfaceAddress.getNetworkPrefixLength();
                    int hostBits = (addressLength * 8) - prefixLength;
                    double hostQuantity = Math.pow(2, hostBits);

                    if (addressLength != 4) {
                        continue;
                    }

                    InetAddress netmask = this.getNetmask(interfaceAddress);

                    BigInteger numericAddress = new BigInteger(netmask.getAddress());
                    byte[] address = new byte[netmask.getAddress().length];

                    /* do not scan the first and the last address */
                    /* 00000000 and 11111111 */
                    hostQuantity -= 2;
                    while (hostQuantity-- > 0) {
                        numericAddress = numericAddress.add(BigInteger.ONE);

                        address = numericAddress.toByteArray();
                        InetAddress a = InetAddress.getByAddress(address);

                        hosts.add(a);
                    }
                }
            }
        } catch (Exception e) {
            // Exception output
        }
        return hosts;
    }

    private InetAddress getNetmask(InterfaceAddress interfaceAddress)
    {
        ArrayList<Byte> netmaskList = new ArrayList<Byte>();
        byte[] address = interfaceAddress.getAddress().getAddress();
        short prefixLength = interfaceAddress.getNetworkPrefixLength();

        byte bit = 0;
        byte octetToAdd = 0;

        for (byte octet : address) {
            octetToAdd = 0;
            for (int i = 0; i < 8; i++) {
                bit = 0;
                bit |= (int) Math.pow(2, i);
                if (prefixLength > 0) {
                    if ((octet | bit) == octet) {
                        octetToAdd |= bit;
                    }
                    prefixLength--;
                }
            }
            netmaskList.add(octetToAdd);
        }

        byte[] netmask = new byte[netmaskList.size()];
        for (int i = 0; i < netmask.length; i++) {
            netmask[i] = (byte) netmaskList.get(i);
        }

        InetAddress host = null;
        try {
            host = InetAddress.getByAddress(netmask);
        } catch (Exception e) {
            Log.d("Rat", e.toString());
        }

        return host;
    }
}
