package com.zhy.lib_common.utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zou on 2016/8/18.
 */
public class DataUtils {
    private static byte[] byte1 = new byte[]{0x0};
    private static byte[] byte2 = new byte[]{0x25};

    private static SimpleDateFormat msFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);

    /**
     * 获取以太网虚拟网卡数据包
     *
     * @param data
     * @param srcIP
     * @param destIP
     * @return
     */
    public static byte[] getEtherFrame(byte[] data, int srcIP, int destIP) {
        return mergerAllByte(byte1, byte2, intToByteArray(destIP - 1), byte1,
                byte2, intToByteArray(srcIP - 1), shortToByteArray(0x8), data);
    }

    /**
     * 获取以太网虚拟网卡数据包，通过ip计算出帧头
     */
    public static byte[] getEtherFrameByIP(byte[] data, int srcIP, int destIP) {
        return mergerAllByte(byte1, byte2, intToByteArray(destIP - 1),
                byte1, byte2, intToByteArray(srcIP - 1),
                shortToByteArray(0x8),
                data);
    }

    /**
     * 获取以太网虚拟网卡数据包
     */
    public static byte[] getEtherFrameByMac(byte[] data, byte[] srcMac, byte[] destMac) {
        return mergerAllByte(destMac, srcMac, shortToByteArray(0x8), data);
    }


    /**
     * 从mac地址转换为byte[]
     *
     * @param macAddress mac 地址
     */
    public static byte[] fromMacString(String macAddress) {
        return HexString.hexStringToByte(macAddress);
    }

    /**
     * 转换mac地址
     *
     * @param dst
     * @return
     */
    public static byte[] parseMacByte(byte[] dst) {
        if (null == dst || dst.length == 0) return dst;
        byte[] src = new byte[6];
        int len = dst.length;
        if (len >= 2) {
            src[0] = dst[0];
            src[1] = dst[1];
            src[2] = dst[len - 1];
            src[3] = len - 2 > 0 ? dst[len - 2] : 0;
            src[4] = len - 3 > 0 ? dst[len - 3] : 0;
            src[5] = len - 4 > 0 ? dst[len - 4] : 0;
        }
        return src;
    }

    /**
     * 合并所有byte[]
     *
     * @param values
     * @return
     */
    public static byte[] mergerAllByte(byte[]... values) {
        int byteLen = 0;
        for (byte[] value : values) {
            byteLen += value.length;
        }
        byte[] totalByte = new byte[byteLen];
        int countLength = 0;
        for (byte[] b : values) {
            System.arraycopy(b, 0, totalByte, countLength, b.length);
            countLength += b.length;
        }
        return totalByte;
    }


    /**
     * 两个byte[]数组头尾相加
     *
     * @param byte1
     * @param byte2
     * @return
     */
    public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
        if (null == byte1) byte1 = new byte[]{};
        if (null == byte2) byte2 = new byte[]{};
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);

        return byte3;
    }

    /**
     * int 转 byte[]
     *
     * @param source
     * @return
     */
    public static byte[] intToByteArray(int source) {
        byte[] bLocalArr = new byte[4];
        for (int i = 0; i < 4; i++) {
            bLocalArr[i] = (byte) (source >> 8 * i & 0xFF);
        }
        return bLocalArr;
    }

    /**
     * int 转 byte[]
     */
    public static byte[] intToByteArrayBigEndian(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * short 转 byte[]
     *
     * @param source
     * @return
     */
    public static byte[] shortToByteArray(int source) {
        byte[] bLocalArr = new byte[2];
        for (int i = 0; i < 2; i++) {
            bLocalArr[i] = (byte) (source >> 8 * i & 0xFF);
        }
        return bLocalArr;
    }

    /**
     * short 转 byte[]
     */
    public static byte[] shortToByteArrayBigEndian(int value) {
        byte[] src = new byte[2];
        src[0] = (byte) ((value >> 8) & 0xFF);
        src[1] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte[] to int
     *
     * @param bs
     * @return
     */
    public static int byteArrayToInt(byte[] bs, int begin) {
        return (bs[begin] & 0xff) | ((bs[begin + 1] << 8) & 0xff00) | ((bs[begin + 2] << 24) >>> 8) | (bs[begin + 3] << 24);
    }

    /**
     * byte[] to IntBigEndian
     *
     * @param bs
     * @return
     */
    public static int byteArrayToIntBigEndian(byte[] bs) {
        return (bs[3] & 0xff) | ((bs[2] << 8) & 0xff00) | ((bs[1] << 24) >>> 8) | (bs[0] << 24);
    }

    /**
     * byteArray To Short
     *
     * @param bs
     * @return
     */
    public static short byteArrayToShort(byte[] bs, int begin) {
        return (short) (((bs[begin + 1] << 8) | bs[begin] & 0xff));
    }

    /**
     * byte[] to IntBigEndian
     *
     * @param begin 起始位置
     */
    public static int byteArrayToIntBigEndian(byte[] bs, int begin) {
        return (bs[begin + 3] & 0xff) | ((bs[begin + 2] << 8) & 0xff00) | ((bs[begin + 1] << 24) >>> 8) | (bs[begin + 0] << 24);
    }

    public static short byteArrayToShortBigEndian(byte[] bs, int begin) {

        return (short) (((bs[begin + 0] << 8) | bs[begin + 1] & 0xff));
    }


    /**
     * int to IP
     *
     * @param ipInt
     * @return
     */
    public static String intToIp(int ipInt) {
        return String.valueOf(((ipInt >> 24) & 0xff)) + '.' +
                ((ipInt >> 16) & 0xff) + '.' +
                ((ipInt >> 8) & 0xff) + '.' + (ipInt & 0xff);
    }


    /**
     * 把IP地址转化为int
     *
     * @param ipAddr
     * @return int
     */
    public static int ipToInt(String ipAddr) {
        try {
            return bytesToInt(ipToBytesByInet(ipAddr));
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }


    /**
     * 根据位运算把 byte[] -> int
     *
     * @param bytes
     * @return int
     */
    private static int bytesToInt(byte[] bytes) {
        int addr = bytes[3] & 0xFF;
        addr |= ((bytes[2] << 8) & 0xFF00);
        addr |= ((bytes[1] << 16) & 0xFF0000);
        addr |= ((bytes[0] << 24) & 0xFF000000);
        return addr;
    }

    /**
     * 把IP地址转化为字节数组
     *
     * @param ipAddr
     * @return byte[]
     */
    private static byte[] ipToBytesByInet(String ipAddr) {
        try {
            return InetAddress.getByName(ipAddr).getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }

    /**
     * 截取byte数组
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);

        return bs;
    }

    /**
     * ip to int
     *
     * @param ip
     * @return
     */
    public static int ip2Int(String ip) {
        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(ip);
            byte[] bytes = inet.getAddress();
            return byteArrayToIntBigEndian(bytes);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String replaceIpSuffix(String ip) {
        StringBuffer buffer = new StringBuffer();
        String[] strings = ip.split("\\.");
        strings[3] = "0";
        for (int i = 0; i < 4; i++) {
            if (i != 3) {
                buffer = buffer.append(strings[i]).append(".");
            } else {
                buffer = buffer.append(strings[i]);
            }
        }

        return buffer.toString();
    }

    /**
     * @param ip
     * @param lan_mask
     * @return
     */
    public static String replaceIpSuffix(String ip, String lan_mask) {
        StringBuffer buffer = new StringBuffer();
        String[] ips = ip.split("\\.");
        String[] lan_masks = lan_mask.split("\\.");
        int ip1, ip2;
        for (int i = 0; i < 4; i++) {
            ip1 = StringUtil.toInteger(ips[i]);
            ip2 = StringUtil.toInteger(lan_masks[i]);
            if (i != 3) {
                buffer = buffer.append(ip1 & ip2).append(".");
            } else {
                buffer = buffer.append(ip1 & ip2);
            }
        }
        return buffer.toString();
    }

    /**
     * @param lanIP
     * @param netMask
     * @return
     */
    public static int lanIP2Int(String lanIP, String netMask) {

        return ip2Int(replaceIpSuffix(lanIP, netMask));
    }


    /**
     * Count the number of 1-bits in a 32-bit integer using a divide-and-conquer strategy
     * see Hacker's Delight section 5.1
     *
     * @param x
     * @return
     */
    public static int pop1bits(int x) {
        x = x - ((x >>> 1) & 0x55555555);
        x = (x & 0x33333333) + ((x >>> 2) & 0x33333333);
        x = (x + (x >>> 4)) & 0x0F0F0F0F;
        x = x + (x >>> 8);
        x = x + (x >>> 16);
        return x & 0x0000003F;
    }


    /**
     * 创建随机字符串
     *
     * @return
     */
    public static String createRandomString() {
        StringBuilder buffer = new StringBuilder();
        char[] ch = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w'
                , 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random r = new Random();
        for (int i = 0; i < r.nextInt(3) + 6; i++) {
            buffer.append(ch[r.nextInt(ch.length)]);
        }
        return buffer.toString();
    }

    /**
     * 获得星期数，星期天从0开始
     *
     * @return
     */
    public static int getWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    /**
     * 获取当前年月日 yyyy-MM-dd格式
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * 判断是否是手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean phoneNumberLocalValidate(EditText phoneNumber) {
        String number = phoneNumber.getText().toString();
        return phoneNumberLocalValidate(number);
    }

    /**
     * 判断是否是手机号码
     *
     * @param number
     * @return
     */
    public static boolean phoneNumberLocalValidate(String number) {
        // 通用号段
//        Pattern pcommon = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0-9])|(17[0-1,6-8])|(18[0-9]))\\d{8}$");
        //只验证第一位是1
        Pattern pcommon = Pattern.compile("^1\\d{10}$");
        return pcommon.matcher(number).matches();
    }

    /**
     * 判断EditText是否为空
     *
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())) return true;
        return false;
    }

    /**
     * 判断密码是否生效
     *
     * @param passwordEditText
     * @param userNameEditText
     * @return
     */
    public static boolean passwordValidate(EditText passwordEditText,
                                           String userNameEditText) {
        return passwordEditText.getText().toString().length() >= 6
                && passwordEditText.getText().toString().length() <= 16
                && !passwordEditText.getText().toString()
                .equals(userNameEditText);
    }

    /**
     * 判断密码是否生效
     *
     * @param passwordEditText
     * @return
     */
    public static boolean isPasswordValidate(EditText passwordEditText) {
        return passwordEditText.getText().toString().length() >= 6
                && passwordEditText.getText().toString().length() <= 16;
    }

    /**
     * 判断用户名是否生效
     *
     * @param accountName
     * @return
     */
    public static boolean accountNameLocalValidate(EditText accountName) {
        if (accountName.getText().toString().length() >= 5
                && accountName.getText().toString().length() <= 16
                && checkAccountName(accountName.getText().toString())
                && !isNumberOnly(accountName)) {
            return true;
        }
        return false;
    }


    /**
     * 检测用户名是否合法
     *
     * @param str
     * @return
     */
    public static boolean checkAccountName(String str) {
        if (TextUtils.isEmpty(str)) return false;
        char ch = ' ';
        boolean isValid = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                    || ch == '-' || (ch >= '0' && ch <= '9')) {
                isValid = true;
            } else {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    /**
     * 检测是否为合法邮箱
     *
     * @param etEmail
     * @return
     */
    public static boolean isLegalEmail(EditText etEmail) {
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email))
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 检测合法共享设备名称
     *
     * @param name
     * @return
     */
    public static boolean isValidateShareDeviceName(String name) {
        if (TextUtils.isEmpty(name)) return true;
        String regEx = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        return m.matches();
    }


    /**
     * 检测是否为纯数字
     *
     * @param accountName
     * @return
     */
    public static boolean isNumberOnly(EditText accountName) {
        String str = accountName.getText().toString();
        return isNumberOnly(str);
    }

    /**
     * 检测string是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumberOnly(String str) {
        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断账号是否为vpn id
     *
     * @return
     */
    public static boolean isVpnID(String account) {

        if (TextUtils.isEmpty(account)) return false;
        if (checkAccountName(account)) return false;
        int index;
        if (account.contains(":")) {
            index = account.indexOf(":");
        } else {
            index = account.indexOf("：");
        }
        String part1, part2;
        try {
            part1 = account.substring(0, index);
            part2 = account.substring(index + 1);
            if (part1.length() > 0 && isNumberOnly(part2) && part2.length() > 2) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }

    /**
     * 毫秒转时间
     */
    public static String timeParse(long duration) {
        String time = "";
        if (duration > 1000) {
            time = timeParseMinute(duration);
        } else {
            long minute = duration / 60000;
            long seconds = duration % 60000;
            long second = Math.round((float) seconds / 1000);
            if (minute < 10) {
                time += "0";
            }
            time += minute + ":";
            if (second < 10) {
                time += "0";
            }
            time += second;
        }
        return time;
    }

    private static String timeParseMinute(long duration) {
        try {
            return msFormat.format(duration);
        } catch (Exception e) {
            e.printStackTrace();
            return "0:00";
        }
    }

    /**
     * ip地址合法校验
     * 1		25[0-5]                     	250-255
     * 2		2[0-4]\\d                    	200-249
     * 3		[1]{1}\\d{1}\\d{1}   		    100-199
     * 4		[1-9]{1}\\d{1}			        10-99
     * 5		\\d{1}				             0-9
     * 6		($|(?!\\.$)\\.)               	结束 或者 不以.结束的加上.
     * 7		(?!^0{1,3}(\\.0{1,3}){3}$)     	排除 0.0.0.0 		(?!^0{1,3}(\\.0{1,3}){3}$)^((25[0-5]|2[0-4]\\d|[1]{1}\\d{1}\\d{1}|[1-9]{1}\\d{1}|\\d{1})($|(?!\\.$)\\.)){4}$
     * 8		(?!^255(\\.255){3}$)      	排除 255.255.255.255	(?!^255(\\.255){3}$)^((25[0-5]|2[0-4]\\d|[1]{1}\\d{1}\\d{1}|[1-9]{1}\\d{1}|\\d{1})($|(?!\\.$)\\.)){4}$
     */
    public static boolean isValidateIP(String ip) {
        Pattern p = Pattern.compile("^((25[0-5]|2[0-4]\\d|[1]{1}\\d{1}\\d{1}|[1-9]{1}\\d{1}|\\d{1})($|(?!\\.$)\\.)){4}$");
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    /**
     * 主机名称合法校验
     *
     * @param hostNmae
     * @return
     */
    public static boolean isValidateHost(String hostNmae) {
        if (TextUtils.isEmpty(hostNmae)) return false;
        Pattern p = Pattern.compile("^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9]).)*([A-Za-z]|[A-Za-z][A-Za-z0-9-]*[A-Za-z0-9])$");
        Matcher m = p.matcher(hostNmae);
        return m.matches();
    }

    /**
     * 检测合法域名
     *
     * @param dnsName
     * @return
     */
    public static boolean isValidDNS(String dnsName) {
        if (TextUtils.isEmpty(dnsName)) return false;
        Pattern p = Pattern.compile("^((http://)|(https://))?([a-zA-Z0-9]([a-zA-Z0-9\\\\-]{0,61}[a-zA-Z0-9])?\\\\.)+[a-zA-Z]{2,6}(/)");
        Matcher m = p.matcher(dnsName);
        return m.matches();
    }


    /**
     * 是否包含表情
     *
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA ||
                codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF ||
                codePoint >= 0xE000 && codePoint <= 0xFFFD;

    }



}
