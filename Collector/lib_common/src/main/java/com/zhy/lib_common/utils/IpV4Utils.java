package com.zhy.lib_common.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by LY on 2017/6/9.
 */

public class IpV4Utils {
    // IpV4的正则表达式，用于判断IpV4地址是否合法
    private static final String IPV4_REGEX = "((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})";

    // 系统子网掩码，它与ip组成一个地址
    private int mask;

    // 1代表A类，2代表B类，3代表C类；4代表其它类型
    public final static int IP_A_TYPE = 1;
    public final static int IP_B_TYPE = 2;
    public final static int IP_C_TYPE = 3;
    public final static int IP_OTHER_TYPE = 4;

    // A类地址范围：1.0.0.1---126.255.255.254
    private static int[] IpATypeRange;
    // B类地址范围：128.0.0.1---191.255.255.254
    private static int[] IpBTypeRange;
    // C类地址范围：192.168.0.0～192.168.255.255
    private static int[] IpCTypeRange;

    // A,B,C类地址的默认mask
    private static int DefaultIpAMask;
    private static int DefaultIpBMask;
    private static int DefaultIpCMask;

    // 初始化
    static {
        IpATypeRange = new int[2];
        IpATypeRange[0] = getIpV4Value("1.0.0.1");
        IpATypeRange[1] = getIpV4Value("126.255.255.254");

        IpBTypeRange = new int[2];
        IpBTypeRange[0] = getIpV4Value("128.0.0.1");
        IpBTypeRange[1] = getIpV4Value("191.255.255.254");

        IpCTypeRange = new int[2];
        IpCTypeRange[0] = getIpV4Value("192.168.0.0");
        IpCTypeRange[1] = getIpV4Value("192.168.255.255");

        DefaultIpAMask = getIpV4Value("255.0.0.0");
        DefaultIpBMask = getIpV4Value("255.255.0.0");
        DefaultIpCMask = getIpV4Value("255.255.255.0");
    }

    /**
     * 默认255.255.255.0
     */
    public IpV4Utils() {
        mask = getIpV4Value("255.255.255.0");
    }

    /**
     * @param masks 任意的如"255.255.254.0"等格式，如果格式不合法，抛出UnknownError异常错误
     */
    public IpV4Utils(String masks) {
        mask = getIpV4Value(masks);
        if (mask == 0) {
            throw new UnknownError();
        }
    }

    public int getMask() {
        return mask;
    }

    /**
     * 比较两个ip地址是否在同一个网段中，如果两个都是合法地址，两个都是非法地址时，可以正常比较；
     * 如果有其一不是合法地址则返回false；
     * 注意此处的ip地址指的是如“192.168.1.1”地址，并不包括mask
     *
     * @return
     */
    public boolean checkSameSegment(String ip1, String ip2) {

        return checkSameSegment(ip1, ip2, mask);
    }

    /**
     * 比较两个ip地址是否在同一个网段中，如果两个都是合法地址，两个都是非法地址时，可以正常比较；
     * 如果有其一不是合法地址则返回false；
     * 注意此处的ip地址指的是如“192.168.1.1”地址
     *
     * @return
     */
    public static boolean checkSameSegment(String ip1, String ip2, int mask) {
        // 判断IPV4是否合法
        if (!ipV4Validate(ip1)) {
            return false;
        }
        if (!ipV4Validate(ip2)) {
            return false;
        }
        int ipValue1 = getIpV4Value(ip1);
        int ipValue2 = getIpV4Value(ip2);
        return (mask & ipValue1) == (mask & ipValue2);
    }

    /**
     * 比较两个ip地址是否在同一个网段中，如果两个都是合法地址，两个都是非法地址时，可以正常比较；
     * 如果有其一不是合法地址则返回false；
     * 注意此处的ip地址指的是如“192.168.1.1”地址
     *
     * @return
     */
    public static boolean checkSameSegmentByDefault(String ip1, String ip2) {
        int mask = getDefaultMaskValue(ip1);     // 获取默认的Mask
        return checkSameSegment(ip1, ip2, mask);
    }

    /**
     * 获取ip值与mask值与的结果
     *
     * @param ipV4
     * @return 32bit值
     */
    public int getSegmentValue(String ipV4) {
        int ipValue = getIpV4Value(ipV4);
        return (mask & ipValue);
    }

    /**
     * 获取ip值与mask值与的结果
     *
     * @return 32bit值
     */
    public static int getSegmentValue(String ip, int mask) {
        int ipValue = getIpV4Value(ip);
        return (mask & ipValue);
    }

    /**
     * 判断ipV4或者mask地址是否合法，通过正则表达式方式进行判断
     *
     * @param ipv4
     */
    public static boolean ipV4Validate(String ipv4) {
        return ipv4Validate(ipv4, IPV4_REGEX);
    }

    private static boolean ipv4Validate(String addr, String regex) {
        if (addr == null) {
            return false;
        } else {
            return Pattern.matches(regex, addr.trim());
        }
    }

    /**
     * 比较两个ip地址，如果两个都是合法地址，则1代表ip1大于ip2，-1代表ip1小于ip2,0代表相等；
     * 如果有其一不是合法地址，如ip2不是合法地址，则ip1大于ip2，返回1，反之返回-1；两个都是非法地址时，则返回0；
     * 注意此处的ip地址指的是如“192.168.1.1”地址，并不包括mask
     *
     * @return
     */
    public static int compareIpV4s(String ip1, String ip2) {
        int result = 0;
        int ipValue1 = getIpV4Value(ip1);     // 获取ip1的32bit值
        int ipValue2 = getIpV4Value(ip2); // 获取ip2的32bit值
        if (ipValue1 > ipValue2) {
            result = -1;
        } else if (ipValue1 <= ipValue2) {
            result = 1;
        }
        return result;
    }

    /**
     * 检测ipV4 的类型，包括A类，B类，C类，其它（C,D和广播）类等
     *
     * @param ipV4
     * @return 返回1代表A类，返回2代表B类，返回3代表C类；返回4代表D类
     */
    public static int checkIpV4Type(String ipV4) {
        int inValue = getIpV4Value(ipV4);
        if (inValue >= IpCTypeRange[0] && inValue <= IpCTypeRange[1]) {
            return IP_C_TYPE;
        } else if (inValue >= IpBTypeRange[0] && inValue <= IpBTypeRange[1]) {
            return IP_B_TYPE;
        } else if (inValue >= IpATypeRange[0] && inValue <= IpATypeRange[1]) {
            return IP_A_TYPE;
        }
        return IP_OTHER_TYPE;
    }

    /**
     * 获取默认mask值，如果IpV4是A类地址，则返回{@linkplain #DefaultIpAMask}，
     * 如果IpV4是B类地址，则返回{@linkplain #DefaultIpBMask}，以此类推
     *
     * @param anyIpV4 任何合法的IpV4
     * @return mask 32bit值
     */
    public static int getDefaultMaskValue(String anyIpV4) {
        int checkIpType = checkIpV4Type(anyIpV4);
        int maskValue = 0;
        switch (checkIpType) {
            case IP_C_TYPE:
                maskValue = DefaultIpCMask;
                break;
            case IP_B_TYPE:
                maskValue = DefaultIpBMask;
                break;
            case IP_A_TYPE:
                maskValue = DefaultIpAMask;
                break;
            default:
                maskValue = DefaultIpCMask;
        }
        return maskValue;
    }

    /**
     * 获取默认mask地址，A类地址对应255.0.0.0，B类地址对应255.255.0.0，
     * C类及其它对应255.255.255.0
     *
     * @param anyIp
     * @return mask 字符串表示
     */
    public static String getDefaultMaskStr(String anyIp) {
        return trans2IpStr(getDefaultMaskValue(anyIp));
    }

    /**
     * 将ip 32bit值转换为如“192.168.0.1”等格式的字符串
     *
     * @param ipValue 32bit值
     * @return
     */
    public static String trans2IpStr(int ipValue) {
        // 保证每一位地址都是正整数
        return ((ipValue >> 24) & 0xff) + "." + ((ipValue >> 16) & 0xff) + "." + ((ipValue >> 8) & 0xff) + "." + (ipValue & 0xff);
    }

    /**
     * 将ip byte数组值转换为如“192.168.0.1”等格式的字符串
     *
     * @param ipBytes 32bit值
     * @return
     */
    public static String trans2IpV4Str(byte[] ipBytes) {
        // 保证每一位地址都是正整数
        return (ipBytes[0] & 0xff) + "." + (ipBytes[1] & 0xff) + "." + (ipBytes[2] & 0xff) + "." + (ipBytes[3] & 0xff);
    }

    public static int getIpV4Value(String ipOrMask) {
        byte[] addr = getIpV4Bytes(ipOrMask);
        int address1 = addr[3] & 0xFF;
        address1 |= ((addr[2] << 8) & 0xFF00);
        address1 |= ((addr[1] << 16) & 0xFF0000);
        address1 |= ((addr[0] << 24) & 0xFF000000);
        return address1;
    }

    public static byte[] getIpV4Bytes(String ipOrMask) {
        try {
            String[] addrs = ipOrMask.split("\\.");
            int length = addrs.length;
            byte[] addr = new byte[length];
            for (int index = 0; index < length; index++) {
                addr[index] = (byte) (Integer.parseInt(addrs[index]) & 0xff);
            }
            return addr;
        } catch (Exception e) {
        }
        return new byte[4];
    }

    /**
     * 获取手机的Ip地址
     */
    public static String getIpAddress(Context context) {
        String IpAddress = null;
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            IpAddress = intToIpAddress(wifiInfo.getIpAddress());
        }
        return IpAddress;
    }

    public static String intToIpAddress(long ipInt) {
        StringBuffer sb = new StringBuffer();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取WifiInfo
     */
    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        if (null != wifiManager) {
            info = wifiManager.getConnectionInfo();
        }
        return info;
    }

    /**
     * @return 手机GPRS网络的IP
     */
    private static String getLocalIpAddress() {
        try {
            //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {//获取IPv4的IP地址
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取WLAN Mac地址
     */
    public static String getMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }


}
