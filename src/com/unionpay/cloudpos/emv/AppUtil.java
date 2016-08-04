
package com.unionpay.cloudpos.emv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

@SuppressLint("DefaultLocale")
class AppUtil
{
    AppUtil() {
    }

    static boolean checkDateYYMM(String expireDate)
    {
        if (expireDate != null && expireDate.length() == 4) {
            int month = Integer.parseInt(expireDate.substring(2));
            return 1 <= month && month <= 12;
        }
        return false;
    }

    static boolean checkDateMMDD(String expireDate) {
        if (expireDate != null && expireDate.length() == 4) {
            int month = Integer.parseInt(expireDate.substring(0, 2));
            int day = Integer.parseInt(expireDate.substring(2));
            return 1 <= month && month <= 12 && 1 <= day && day <= 31;
        }
        return false;
    }

    /**
     * Method Check whether time is legal.<br>
     * 
     * @param timeData The int value to be checked.<br>
     */
    static boolean checkTime(String timeData)
    {
        if (timeData != null && timeData.length() == 4) {
            int hh = Integer.parseInt(timeData.substring(0, 2));
            int mm = Integer.parseInt(timeData.substring(2, 4));
            return 0 <= hh && hh <= 23 && 0 <= mm && mm <= 59;
        }
        return false;
    }

    static String formatDateTime(String mmdd, String hhmmss) {
        return new StringBuffer(hhmmss)
                .insert(4, ":")
                .insert(2, ":")
                .insert(0, ' ')
                .insert(0, mmdd)
                .insert(2, "/")
                .toString();
    }

    /**
     * Protects PAN, Track2, CVC (suitable for logs).
     * 
     * <pre>
     * "40000101010001" is converted to "400001____0001"
     * "40000101010001=020128375" is converted to "400001____0001=0201_____"
     * "123" is converted to "___"
     * </pre>
     * 
     * @param s string to be protected
     * @return 'protected' String
     */
    static String cardProtect(String s)
    {
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        int clear = len > 6 ? 6 : 0;
        int lastFourIndex = -1;
        if (clear > 0) {
            lastFourIndex = s.indexOf('=') - 4;
            if (lastFourIndex < 0) {
                lastFourIndex = s.indexOf('^') - 4;
                if (lastFourIndex < 0) {
                    lastFourIndex = len - 4;
                }
            }
        }
        for (int i = 0; i < len; ++i) {
            char theChar = s.charAt(i);
            if (theChar == '=') {
                clear = 5;
            } else if (theChar == '^') {
                lastFourIndex = 0;
                clear = len - i;
            } else if (i == lastFourIndex) {
                clear = 4;
            }
            sb.append(--clear >= 0 ? theChar : '*');
        }
        return sb.toString();
    }

    static String cardProtect(String s, int header, int tailer) {
        StringBuffer sb = new StringBuffer();
        int len = s.length();
        int clear = header < len ? header : 0;
        int lastIndex = len - tailer;

        for (int i = 0; i < len; i++) {
            if (i == lastIndex) {
                clear = tailer;
            }
            sb.append(clear-- > 0 ? s.charAt(i) : '*');
        }
        return sb.toString();
    }

    static String formatPAN(String pan) {
        return pan == null || pan.length() < 13 || pan.length() > 19 ? pan
                : pan.length() <= 16 ? pan.replaceAll("(\\d{4})(\\d{4})(\\d{4})(.*$)",
                        "$1 $2 $3 $4")
                        : pan.replaceAll("(\\d{4})(\\d{4})(\\d{4})(\\d{4})(.*$)", "$1 $2 $3 $4 $5");
    }

    static String convertExpDate(String expDate) {
        if (expDate != null) { // 除 BUG: 居然悄悄地强制要求长度不低于2!!! DuanCS@[20150115]
            int length = expDate.length();
            if (2 < length) {
                expDate = expDate.substring(0, 2) + expDate.substring(length - 2, length);
            }
        }
        return expDate;
    }

    /**
     * 从2磁道数据中取有效期
     */
    static String getExpirationFromTrack2(String track2) {
        return getCardInfoFromTrack2(track2, 0);
    }

    /**
     * 从2磁道数据中取卡号
     */
    static String getCardNoFromTrack2(String track2) {
        return getCardInfoFromTrack2(track2, 1);
    }

    /**
     * 从2磁道数据中取指定信息<br>
     * type: 0 - 有效期<br>
     * 1 - 卡号<br>
     */
    static String getCardInfoFromTrack2(String track2, int type) {
        String result = "";

        if (track2 != null) {
            int panStart = -1;
            int panEnd = -1;
            for (int i = 0; i < track2.length(); i++) {
                char theChar = track2.charAt(i);
                if (panStart == -1 && ('0' <= theChar && theChar <= '9')) {
                    panStart = i;
                }
                if (theChar == '=' || theChar == 'D') { // Field separator
                    panEnd = i;
                    break;
                }
            }

            if (panEnd != -1 && panStart != -1) {
                result = type == 0 ? track2.substring(panEnd + 1, panEnd + 1 + 4)
                        : type == 1 ? track2.substring(panStart, panEnd)
                                : result;
            }
        }

        return result;
    }

    @SuppressLint("DefaultLocale")
    static String convertAmount(String amount) {
        try { // 除BUG: 当格式不正确时, 原样返回!!! DuanCS@[20150115]
            long amountValue = Long.parseLong(amount);
            amount = String.format("%.2f", amountValue / 100.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    /**
     * 算法优化. DuanCS@[20150113]
     */
    static final int FA_SEPARATOR = 0x80000000;

    static String formatAmount(String amount) { // 默认: 分隔符 + 2位小数
        return formatAmount(amount, FA_SEPARATOR | 2);
    }

    static String formatAmount(long amount) { // 默认: 分隔符 + 2位小数
        return formatAmount(amount, FA_SEPARATOR | 2);
    }

    static String formatAmount(String amount, boolean separator) { // 默认: 2位小数
        return formatAmount(amount, (separator ? FA_SEPARATOR : 0) | 2);
    }

    static String formatAmount(long amount, boolean separator) { // 默认: 2位小数
        return formatAmount(amount, (separator ? FA_SEPARATOR : 0) | 2);
    }

    static String formatAmount(String amount, boolean separator, int decimal) {
        return formatAmount(amount, (separator ? FA_SEPARATOR : 0) | (decimal & ~FA_SEPARATOR));
    }

    static String formatAmount(long amount, boolean separator, int decimal) {
        return formatAmount(amount, (separator ? FA_SEPARATOR : 0) | (decimal & ~FA_SEPARATOR));
    }

    /**
     * 整数字符串 转成 金额字符串<br>
     * 例: ("00123456789", FA_SEPARATOR | 4) -> "12,345.6789"<br>
     */
    static String formatAmount(String amount, int mode) { // mode: 分隔符、小数位数
        long lValue = amount == null || amount.length() == 0 ? 0 : Long.parseLong(amount);
        return formatAmount(lValue, mode);
    }

    /**
     * 数值 转成 金额字符串<br>
     * mode: (按位组合)<br>
     * FA_SEPARATOR - 整数部分带分隔符(逗号)<br>
     * 数值 - 小数位数(<=7)<br>
     * 例: (123456789, FA_SEPARATOR | 4) -> "12,345.6789"<br>
     */
    static String formatAmount(long amount, int mode) {
        boolean separator = (mode & FA_SEPARATOR) != 0;
        int decimal = mode & 0x0F;
        String sign = "";
        if (amount < 0) {
            sign = "-";
            amount = -amount;
        }

        // 小数部分
        String sDecimal = null;
        if (0 < decimal) {
            decimal = (int) Math.pow(10, Math.min(0x07, decimal)); // 至多只支持7位小数
            sDecimal = String.format("%d", decimal + (amount % decimal));
            amount /= decimal;
        }

        // 整数部分
        String sAmount = String.format(separator ? "%s%,d" : "%s%d", sign, amount); // ',':
                                                                                    // 每组3位用逗号隔开

        return sDecimal == null ? sAmount : sAmount + '.' + sDecimal.substring(1);
    }

    static int parseAmount(String strAmount) { // 逻辑仍然杂乱... DuanCS@[20150114]
        int amount = -1;
        byte[] bytes = strAmount.getBytes();
        byte[] temp = new byte[bytes.length];
        byte[] tempPoint = {
                '0', '0'
        };
        int index = 0, indexPoint = 0;
        boolean pointFlag = false;

        if (bytes[0] != '.') {
            for (int i = 0; i < bytes.length; ++i) {
                byte theByte = bytes[i];
                if (theByte != '.' && (theByte < '0' || '9' < theByte)) {
                    return amount;
                }

                if (pointFlag) {
                    if (theByte == '.') {
                        return amount;
                    }
                    tempPoint[indexPoint++] = theByte;
                    if (indexPoint >= 2) {
                        break;
                    }
                } else if (theByte == '.') {
                    pointFlag = true;
                } else {
                    temp[index++] = theByte;
                }
            }
            if (!(indexPoint == 1 || (indexPoint == 0 && pointFlag))) {
                byte[] temp2 = new byte[index + 2];
                System.arraycopy(temp, 0, temp2, 0, index);
                System.arraycopy(tempPoint, 0, temp2, index, 2);
                amount = NumberUtil.parseInt(temp2, 0, 10, false);
            }
        }
        return amount;
    }

    static int toAmount(byte[] byte6) {
        int amount = -1;

        try {
            amount = Integer.parseInt(ByteUtil.arrayToHexStr(byte6));
        } catch (NumberFormatException e) {
            // using default value only
        }

        return amount;
    }

    /**
     * 转换成 金额 字节数组 (12位,或压缩成6位BCD)
     */
    static byte[] toCurrency(long number, boolean bcdFlag) {
        String str = String.format("%012d", number);
        return bcdFlag ? StringUtil.hexString2bytes(str) : str.getBytes();
    }

    static byte[] removeTailF(byte[] buffer) {
        return removeTail(buffer, (byte) 'F');
    }

    /**
     * 从尾部连续删除指定的字节, 返回新数组
     */
    static byte[] removeTail(byte[] buffer, byte value) {
        if (buffer != null) {
            int length = buffer.length;
            while (--length >= 0 && buffer[length] == value)
                // 除错: 居然用 "!=". DuanCS@[20150120]
                ;
            if (++length != buffer.length) { // 除BUG: 居然 length == 0 时仍然返回
                                             // buffer 本身! DuanCS@[20150121]
                byte[] destBuffer = new byte[length];
                System.arraycopy(buffer, 0, destBuffer, 0, length);
                buffer = destBuffer;
            }
        }
        return buffer;
    }

    /**
     * 让当前线程暂停一段时间. DuanCS@[20140811]<br>
     * 若被中断，返回 false(否则true)<br>
     */
    static boolean trySleep(long millis) {
        boolean interrupted = false;
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            interrupted = true;
        }
        return !interrupted;
    }

    static String getSystemProperty(String name) {
        Object bootloaderVersion = null;
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            // Log.i("systemProperties", systemProperties.toString());
            bootloaderVersion = systemProperties.getMethod("get", new Class[] {
                    String.class, String.class
            }).invoke(systemProperties, new Object[] {
                    name, "unknown"
            });
            // Log.i("bootloaderVersion",
            // bootloaderVersion.getClass().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bootloaderVersion.toString();
    }

    /**
     * 功能: 按 name、value 对, 自动创建 Bundle. DuanCS@[20150917]<br>
     * 注意: 1.name 须为 String 类型, 其首字母决定 value 的类型 - a:byte[], b:boolean, i:int,
     * l:long, s:String<br>
     * 2.若无配对value, 该name将被丢弃<br>
     * 3.若首元素为 Bundle,则附加到该Bundle(否则新建),并最终返回它<br>
     */
    static Bundle makeBundle(Object... nameValuePairs) {
        Bundle bundle = null;
        int from = 0, to = nameValuePairs.length;

        if (0 < to) {
            if (nameValuePairs[0] instanceof Bundle) {
                bundle = (Bundle) nameValuePairs[0];
                ++from;
                --to;
            } else if (1 < to) {
                bundle = new Bundle();
            }
            for (--to; from < to;) {
                String key = (String) nameValuePairs[from++];
                Object value = nameValuePairs[from++];
                switch (key.charAt(0)) {
                    case 's':
                        bundle.putString(key, (String) value);
                        break;
                    case 'l':
                        bundle.putLong(key, ((Long) value).longValue());
                        break;
                    case 'i':
                        bundle.putInt(key, ((Integer) value).intValue());
                        break;
                    case 'a':
                        bundle.putByteArray(key, (byte[]) value);
                        break;
                    case 'b':
                        bundle.putBoolean(key, ((Boolean) value).booleanValue());
                        break;
                }
            }
        }

        return bundle;
    }

    /**
     * 显示桌面
     */
    static void showDesktop(Context ctx) {
        Intent mHomeIntent;
        mHomeIntent = new Intent(Intent.ACTION_MAIN, null);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        ctx.startActivity(mHomeIntent);
    }
}
