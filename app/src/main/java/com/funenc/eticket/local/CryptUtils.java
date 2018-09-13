package com.funenc.eticket.local;

public class CryptUtils {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static native byte[] sha1(byte in[], int length);

    public static native byte[] sm3(byte in[], int length);

    public static native byte[] aesEnc(byte in[], int length, byte key[]);

    public static native byte[] aesDec(byte in[], int length, byte key[]);

    public static native byte[] sm4Dec(byte in[], int length);

    public static native byte[] sm2Enc(byte in[], int length);

    public static native byte[] sm2Dec(byte in[], int length);

    public static native byte[] sm2Sign(byte in[], int length, byte key[], int keyLength);

    public static native int sm2Verify(byte in[], int length, byte sign[], int signLen);

    public static native int genSM2KeyPairs(String path);
}
