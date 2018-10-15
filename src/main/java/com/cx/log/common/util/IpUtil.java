package com.cx.log.common.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author: cx
 * @Description:
 * @Date: Created in 17:07 2018/9/14
 */
public class IpUtil {
    private static final String NETWORK_CARD = "eth0";
    private static final String NETWORK_CARD_BAND = "bond0";
    private static String ip = localIP();

    public IpUtil() {
    }

    public static String getLocalHostName() {
        try {
            InetAddress e = InetAddress.getLocalHost();
            return e.getHostName();
        } catch (Exception var1) {
            var1.printStackTrace();
            return "";
        }
    }

    public static String getLinuxLocalIP() {
        String ip = "";

        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            NetworkInterface ni;
            do {
                if (!e.hasMoreElements()) {
                    return ip;
                }

                ni = (NetworkInterface) e.nextElement();
            } while (!"eth0".equals(ni.getName()) && !"bond0".equals(ni.getName()));

            Enumeration e2 = ni.getInetAddresses();

            while (e2.hasMoreElements()) {
                InetAddress ia = (InetAddress) e2.nextElement();
                if (!(ia instanceof Inet6Address)) {
                    ip = ia.getHostAddress();
                }
            }
        } catch (SocketException var5) {
            var5.printStackTrace();
        }

        return ip;
    }

    public static String getWinLocalIP() {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress().toString();
        } catch (Exception e) {

        } finally {
        }
        return ip;
    }

    public static String getLocalIP() {
        return ip;
    }

    public static String localIP() {
        String ip = null;
        if (!System.getProperty("os.name").contains("Win")) {
            ip = getLinuxLocalIP();
        } else {
            ip = getWinLocalIP();
        }

        return ip;
    }
}
