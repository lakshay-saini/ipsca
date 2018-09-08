package com.decipher.ipsca;


import com.decipher.ipsca.pojo.Credentials;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.IntStream;


public class Application {

    public static void main(String[] args){
        String operatingSystem = System.getProperty("os.name");
        System.out.println("operatingSystem = " + operatingSystem);
//        try {
//            Process exec = Runtime.getRuntime().exec("sshpass -p 'decipher@123' ssh -o StrictHostKeyChecking=no arun@192.168.0.142 'cat /home/arun/Desktop/TextFile.txt'");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            shutdownPc(operatingSystem);
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//        List<String> networkIPs = getNetworkIPs();
//        getCredentials(networkIPs);

    }

    private static void shutdownPc(String operatingSystem) {
        String shutdownCommand;
        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
            shutdownCommand = "shutdown -h now";
        }
        else if ("Windows".equals(operatingSystem)) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }
        else {
            throw new RuntimeException("Unsupported operating system.");
        }

        try {
            Runtime.getRuntime().exec(shutdownCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Map<String, Credentials> getCredentials(List<String> networkIPs) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Credentials> ipList = new HashMap<>();
        for (String networkIP : networkIPs) {
            Credentials credentials = new Credentials();
            System.out.print(networkIP + " @user: ");
            String user = scanner.nextLine();
            credentials.setUser(user);
            System.out.print("password: ");
            String password = scanner.nextLine();
            credentials.setPassPhrase(password);
            ipList.put(networkIP, credentials);
        }
        return ipList;
    }


    private static List<String> getNetworkIPs() {
        List<String> ips = new ArrayList<>();
        final byte[] ip;
        try {
            ip = InetAddress.getLocalHost().getAddress();
        } catch (Exception e) {
            return ips;
        }

        IntStream.range(1, 255).forEach(j -> new Thread(() -> {
            try {
                ip[3] = (byte) j;
                InetAddress address = InetAddress.getByAddress(ip);
                String output = address.toString().substring(1);
                if (address.isReachable(5000)) {
                    ips.add(output);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());

        return ips;
    }

    public static boolean shutdown(int time) throws IOException {
        String shutdownCommand, t = time == 0 ? "now" : String.valueOf(time);

        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC|| SystemUtils.IS_OS_MAC_OSX)
            shutdownCommand = "shutdown -h " + t;
        else if(SystemUtils.IS_OS_WINDOWS_XP || SystemUtils.IS_OS_WINDOWS_VISTA || SystemUtils.IS_OS_WINDOWS_7)
            shutdownCommand = "shutdown.exe -s -t " + t;
        else
            return false;

        Runtime.getRuntime().exec(shutdownCommand);
        return true;
    }
}
