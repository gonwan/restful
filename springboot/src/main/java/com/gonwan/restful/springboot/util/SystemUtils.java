package com.gonwan.restful.springboot.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class SystemUtils {

    public static String getLocalHost() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.getName().startsWith("docker")) { /* workaround */
                    continue;
                }
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet4Address && !ia.isLoopbackAddress() && !ia.isLinkLocalAddress()) {
                        return ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            /* ignore */
        }
        return "";
    }

}
