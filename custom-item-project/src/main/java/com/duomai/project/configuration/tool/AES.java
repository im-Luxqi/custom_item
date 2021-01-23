package com.duomai.project.configuration.tool;


import com.duomai.project.tool.CommonDateParseUtil;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Date;


public class AES {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AES.class);

    private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static final int[] IA = new int[256];

    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        sSrc = sSrc + "__" + CommonDateParseUtil.date2string(new Date(), "yyyyMMddHHmmss");
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return encodeToString(encrypted, false);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey, boolean isCheckDate) throws Exception {

        if (isCheckDate) {
            try {
                // 判断Key是否正确
                if (sKey == null) {
                    System.out.print("Key为空null");
                    return null;
                }
                // 判断Key是否为16位
                if (sKey.length() != 16) {
                    System.out.print("Key长度不是16位");
                    return null;
                }
                byte[] raw = sKey.getBytes("ASCII");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                byte[] encrypted1 = decode(sSrc);//先用base64解密
                try {
                    byte[] original = cipher.doFinal(encrypted1);
                    String originalString = new String(original);
                    String[] yStr = originalString.split("__");
                    String timeString = "";
                    if (yStr.length < 2) {
                        logger.error("Decrypt error yStr.length =", yStr.length);
                        return null;
                    } else if (yStr.length == 2) {
                        originalString = yStr[0];
                        timeString = yStr[1];
                    } else {
                        timeString = yStr[yStr.length - 1];
                        originalString = yStr[0];
                        for (int i = 1; i < yStr.length - 1; i++) {
                            originalString += "__" + yStr[i];
                        }
                    }
                    timeString = timeString.substring(0, 4) + "-" + timeString.substring(4, 6) + "-" + timeString.substring(6, 8) + " " + timeString.substring(8, 10) + ":" + timeString.substring(10, 12) + ":" + timeString.substring(12, 14);
                    Date timeSet = CommonDateParseUtil.string2date(timeString, "yyyy-MM-dd HH:mm:ss");
                    //当前时间值超过了7天需要重新构建session
                    if (CommonDateParseUtil.differMinute(timeSet, new Date()) <= 10080) {
                        return originalString;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                    logger.error("Decrypt error", e);
                    return null;
                }
            } catch (Exception ex) {
                logger.error("Decrypt error", ex);
                return null;
            }
        } else {
            return Decrypt(sSrc, sKey);
        }

    }

    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                String[] yStr = originalString.split("__");
                if (yStr.length < 2) {
                    return null;
                } else if (yStr.length == 2) {
                    originalString = yStr[0];
                } else {
                    originalString = yStr[0];
                    for (int i = 1; i < yStr.length - 1; i++) {
                        originalString += "__" + yStr[i];
                    }
                }
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                logger.error("Decrypt error", e);
                return null;
            }
        } catch (Exception ex) {
            logger.error("Decrypt error", ex);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        /*
         * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
         * 此处使用AES-128-CBC加密模式，key需要为16位。
         */
        String cKey = "47HTvV7XOty3bQlE";
//        // 需要加密的字串
        String cSrc = "舞017qC3mgwNqMxSzWCNKOe008WonENxO4Za+beA8+VO+is=";
        System.out.println(cSrc);
//        // 加密
        long lStart = System.currentTimeMillis();
        String enString = AES.Encrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);
//
        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");
//        // 解密
        lStart = System.currentTimeMillis();
        String DeString = AES.Decrypt(enString, cKey, true);
        System.out.println("解密后的字串是：" + DeString);
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("解密耗时：" + lUseTime + "毫秒");

//    	String s = new String(new BASE64Decoder().decodeBuffer("sPcpD4i4XP1j47trVuF5pkdOWZrZtdaRq/8+DHuzBhW0nhW+BWrd5hEgu6ZrcvME"), "UTF-8");
//    	System.out.println(s);


    }

    public static final char[] encodeToChar(byte[] sArr, boolean lineSep) {
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0) {
            return new char[0];
        } else {
            int eLen = sLen / 3 * 3;
            int cCnt = (sLen - 1) / 3 + 1 << 2;
            int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0);
            char[] dArr = new char[dLen];
            int left = 0;
            int d = 0;
            int cc = 0;

            while (left < eLen) {
                int i = (sArr[left++] & 255) << 16 | (sArr[left++] & 255) << 8 | sArr[left++] & 255;
                dArr[d++] = CA[i >>> 18 & 63];
                dArr[d++] = CA[i >>> 12 & 63];
                dArr[d++] = CA[i >>> 6 & 63];
                dArr[d++] = CA[i & 63];
                if (lineSep) {
                    ++cc;
                    if (cc == 19 && d < dLen - 2) {
                        dArr[d++] = '\r';
                        dArr[d++] = '\n';
                        cc = 0;
                    }
                }
            }

            left = sLen - eLen;
            if (left > 0) {
                d = (sArr[eLen] & 255) << 10 | (left == 2 ? (sArr[sLen - 1] & 255) << 2 : 0);
                dArr[dLen - 4] = CA[d >> 12];
                dArr[dLen - 3] = CA[d >>> 6 & 63];
                dArr[dLen - 2] = left == 2 ? CA[d & 63] : 61;
                dArr[dLen - 1] = '=';
            }

            return dArr;
        }
    }

    public static final byte[] decode(char[] sArr) {
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0) {
            return new byte[0];
        } else {
            int sepCnt = 0;

            int pad;
            for (pad = 0; pad < sLen; ++pad) {
                if (IA[sArr[pad]] < 0) {
                    ++sepCnt;
                }
            }

            if ((sLen - sepCnt) % 4 != 0) {
                return null;
            } else {
                pad = 0;
                int len = sLen;

                while (len > 1) {
                    --len;
                    if (IA[sArr[len]] > 0) {
                        break;
                    }

                    if (sArr[len] == '=') {
                        ++pad;
                    }
                }

                len = ((sLen - sepCnt) * 6 >> 3) - pad;
                byte[] dArr = new byte[len];
                int s = 0;
                int d = 0;

                while (d < len) {
                    int i = 0;

                    for (int j = 0; j < 4; ++j) {
                        int c = IA[sArr[s++]];
                        if (c >= 0) {
                            i |= c << 18 - j * 6;
                        } else {
                            --j;
                        }
                    }

                    dArr[d++] = (byte) (i >> 16);
                    if (d < len) {
                        dArr[d++] = (byte) (i >> 8);
                        if (d < len) {
                            dArr[d++] = (byte) i;
                        }
                    }
                }

                return dArr;
            }
        }
    }

    public static final byte[] decodeFast(char[] sArr) {
        int sLen = sArr.length;
        if (sLen == 0) {
            return new byte[0];
        } else {
            int sIx = 0;

            int eIx;
            for (eIx = sLen - 1; sIx < eIx && IA[sArr[sIx]] < 0; ++sIx) {
            }

            while (eIx > 0 && IA[sArr[eIx]] < 0) {
                --eIx;
            }

            int pad = sArr[eIx] == '=' ? (sArr[eIx - 1] == '=' ? 2 : 1) : 0;
            int cCnt = eIx - sIx + 1;
            int sepCnt = sLen > 76 ? (sArr[76] == '\r' ? cCnt / 78 : 0) << 1 : 0;
            int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
            byte[] dArr = new byte[len];
            int d = 0;
            int i = 0;
            int r = len / 3 * 3;

            while (d < r) {
                i = IA[sArr[sIx++]] << 18 | IA[sArr[sIx++]] << 12 | IA[sArr[sIx++]] << 6 | IA[sArr[sIx++]];
                dArr[d++] = (byte) (i >> 16);
                dArr[d++] = (byte) (i >> 8);
                dArr[d++] = (byte) i;
                if (sepCnt > 0) {
                    ++i;
                    if (i == 19) {
                        sIx += 2;
                        i = 0;
                    }
                }
            }

            if (d < len) {
                i = 0;

                for (r = 0; sIx <= eIx - pad; ++r) {
                    i |= IA[sArr[sIx++]] << 18 - r * 6;
                }

                for (r = 16; d < len; r -= 8) {
                    dArr[d++] = (byte) (i >> r);
                }
            }

            return dArr;
        }
    }

    public static final byte[] encodeToByte(byte[] sArr, boolean lineSep) {
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0) {
            return new byte[0];
        } else {
            int eLen = sLen / 3 * 3;
            int cCnt = (sLen - 1) / 3 + 1 << 2;
            int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0);
            byte[] dArr = new byte[dLen];
            int left = 0;
            int d = 0;
            int cc = 0;

            while (left < eLen) {
                int i = (sArr[left++] & 255) << 16 | (sArr[left++] & 255) << 8 | sArr[left++] & 255;
                dArr[d++] = (byte) CA[i >>> 18 & 63];
                dArr[d++] = (byte) CA[i >>> 12 & 63];
                dArr[d++] = (byte) CA[i >>> 6 & 63];
                dArr[d++] = (byte) CA[i & 63];
                if (lineSep) {
                    ++cc;
                    if (cc == 19 && d < dLen - 2) {
                        dArr[d++] = 13;
                        dArr[d++] = 10;
                        cc = 0;
                    }
                }
            }

            left = sLen - eLen;
            if (left > 0) {
                d = (sArr[eLen] & 255) << 10 | (left == 2 ? (sArr[sLen - 1] & 255) << 2 : 0);
                dArr[dLen - 4] = (byte) CA[d >> 12];
                dArr[dLen - 3] = (byte) CA[d >>> 6 & 63];
                dArr[dLen - 2] = left == 2 ? (byte) CA[d & 63] : 61;
                dArr[dLen - 1] = 61;
            }

            return dArr;
        }
    }

    public static final byte[] decode(byte[] sArr) {
        int sLen = sArr.length;
        int sepCnt = 0;

        int pad;
        for (pad = 0; pad < sLen; ++pad) {
            if (IA[sArr[pad] & 255] < 0) {
                ++sepCnt;
            }
        }

        if ((sLen - sepCnt) % 4 != 0) {
            return null;
        } else {
            pad = 0;
            int len = sLen;

            while (len > 1) {
                --len;
                if (IA[sArr[len] & 255] > 0) {
                    break;
                }

                if (sArr[len] == 61) {
                    ++pad;
                }
            }

            len = ((sLen - sepCnt) * 6 >> 3) - pad;
            byte[] dArr = new byte[len];
            int s = 0;
            int d = 0;

            while (d < len) {
                int i = 0;

                for (int j = 0; j < 4; ++j) {
                    int c = IA[sArr[s++] & 255];
                    if (c >= 0) {
                        i |= c << 18 - j * 6;
                    } else {
                        --j;
                    }
                }

                dArr[d++] = (byte) (i >> 16);
                if (d < len) {
                    dArr[d++] = (byte) (i >> 8);
                    if (d < len) {
                        dArr[d++] = (byte) i;
                    }
                }
            }

            return dArr;
        }
    }

    public static final byte[] decodeFast(byte[] sArr) {
        int sLen = sArr.length;
        if (sLen == 0) {
            return new byte[0];
        } else {
            int sIx = 0;

            int eIx;
            for (eIx = sLen - 1; sIx < eIx && IA[sArr[sIx] & 255] < 0; ++sIx) {
            }

            while (eIx > 0 && IA[sArr[eIx] & 255] < 0) {
                --eIx;
            }

            int pad = sArr[eIx] == 61 ? (sArr[eIx - 1] == 61 ? 2 : 1) : 0;
            int cCnt = eIx - sIx + 1;
            int sepCnt = sLen > 76 ? (sArr[76] == 13 ? cCnt / 78 : 0) << 1 : 0;
            int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
            byte[] dArr = new byte[len];
            int d = 0;
            int i = 0;
            int r = len / 3 * 3;

            while (d < r) {
                i = IA[sArr[sIx++]] << 18 | IA[sArr[sIx++]] << 12 | IA[sArr[sIx++]] << 6 | IA[sArr[sIx++]];
                dArr[d++] = (byte) (i >> 16);
                dArr[d++] = (byte) (i >> 8);
                dArr[d++] = (byte) i;
                if (sepCnt > 0) {
                    ++i;
                    if (i == 19) {
                        sIx += 2;
                        i = 0;
                    }
                }
            }

            if (d < len) {
                i = 0;

                for (r = 0; sIx <= eIx - pad; ++r) {
                    i |= IA[sArr[sIx++]] << 18 - r * 6;
                }

                for (r = 16; d < len; r -= 8) {
                    dArr[d++] = (byte) (i >> r);
                }
            }

            return dArr;
        }
    }

    public static final String encodeToString(byte[] sArr, boolean lineSep) {
        return new String(encodeToChar(sArr, lineSep));
    }

    public static final byte[] decode(String str) {
        int sLen = str != null ? str.length() : 0;
        if (sLen == 0) {
            return new byte[0];
        } else {
            int sepCnt = 0;

            int pad;
            for (pad = 0; pad < sLen; ++pad) {
                if (IA[str.charAt(pad)] < 0) {
                    ++sepCnt;
                }
            }

            if ((sLen - sepCnt) % 4 != 0) {
                return null;
            } else {
                pad = 0;
                int len = sLen;

                while (len > 1) {
                    --len;
                    if (IA[str.charAt(len)] > 0) {
                        break;
                    }

                    if (str.charAt(len) == '=') {
                        ++pad;
                    }
                }

                len = ((sLen - sepCnt) * 6 >> 3) - pad;
                byte[] dArr = new byte[len];
                int s = 0;
                int d = 0;

                while (d < len) {
                    int i = 0;

                    for (int j = 0; j < 4; ++j) {
                        int c = IA[str.charAt(s++)];
                        if (c >= 0) {
                            i |= c << 18 - j * 6;
                        } else {
                            --j;
                        }
                    }

                    dArr[d++] = (byte) (i >> 16);
                    if (d < len) {
                        dArr[d++] = (byte) (i >> 8);
                        if (d < len) {
                            dArr[d++] = (byte) i;
                        }
                    }
                }

                return dArr;
            }
        }
    }

    public static final boolean isBase64Value(String str) {
        int sLen = str != null ? str.length() : 0;
        if (sLen == 0) {
            return false;
        } else {
            int sepCnt = 0;

            for (int i = 0; i < sLen; ++i) {
                if (IA[str.charAt(i)] < 0) {
                    ++sepCnt;
                }
            }

            if ((sLen - sepCnt) % 4 != 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static final byte[] decodeFast(String s) {
        int sLen = s.length();
        if (sLen == 0) {
            return new byte[0];
        } else {
            int sIx = 0;

            int eIx;
            for (eIx = sLen - 1; sIx < eIx && IA[s.charAt(sIx) & 255] < 0; ++sIx) {
            }

            while (eIx > 0 && IA[s.charAt(eIx) & 255] < 0) {
                --eIx;
            }

            int pad = s.charAt(eIx) == '=' ? (s.charAt(eIx - 1) == '=' ? 2 : 1) : 0;
            int cCnt = eIx - sIx + 1;
            int sepCnt = sLen > 76 ? (s.charAt(76) == '\r' ? cCnt / 78 : 0) << 1 : 0;
            int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
            byte[] dArr = new byte[len];
            int d = 0;
            int i = 0;
            int r = len / 3 * 3;

            while (d < r) {
                i = IA[s.charAt(sIx++)] << 18 | IA[s.charAt(sIx++)] << 12 | IA[s.charAt(sIx++)] << 6 | IA[s.charAt(sIx++)];
                dArr[d++] = (byte) (i >> 16);
                dArr[d++] = (byte) (i >> 8);
                dArr[d++] = (byte) i;
                if (sepCnt > 0) {
                    ++i;
                    if (i == 19) {
                        sIx += 2;
                        i = 0;
                    }
                }
            }

            if (d < len) {
                i = 0;

                for (r = 0; sIx <= eIx - pad; ++r) {
                    i |= IA[s.charAt(sIx++)] << 18 - r * 6;
                }

                for (r = 16; d < len; r -= 8) {
                    dArr[d++] = (byte) (i >> r);
                }
            }

            return dArr;
        }
    }

    static {
        Arrays.fill(IA, -1);
        int i = 0;

        for (int iS = CA.length; i < iS; IA[CA[i]] = i++) {
        }

        IA[61] = 0;
    }
}
