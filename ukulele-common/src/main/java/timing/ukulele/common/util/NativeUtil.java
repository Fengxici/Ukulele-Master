package timing.ukulele.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class NativeUtil {

    private NativeUtil() {
    }

    /**
     * 获取机器名
     *
     * @return 获取机器名
     */
    public static String getHostName() {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }

    /**
     * 获取网卡序列号
     *
     * @return 获取网卡序列号
     */
    public static String getDUID() {
        String address = "";
        String command = "cmd.exe /c ipconfig /all";
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("DUID") > 0) {
                    int index = line.indexOf(":");
                    index += 2;
                    address = line.substring(index);
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}
