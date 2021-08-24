package com.Ghannely_Encrypt_Decrypt_Tracks;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Amany on 19-Mar-17.
 */

public class Encryption_DecryptionAudio {
    private static final String TAG = "smsfwd";
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CTR/NoPadding";
    public static File mEncryptedFile;
    public static Cipher mCipher;
    public static SecretKeySpec mSecretKeySpec;
    public static IvParameterSpec mIvParameterSpec;
    // Replace me with a 16-byte key, share between Java and C#
    public static byte[] rawSecretKey = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static String MESSAGEDIGEST_ALGORITHM = "MD5";
    private static String ENCRYPTED_FILE_NAME;
    private byte[] passwordKey;

    public Encryption_DecryptionAudio(String passphrase) {
        passwordKey = encodeDigest(passphrase);

        mSecretKeySpec = new SecretKeySpec(passwordKey, AES_ALGORITHM);
        mIvParameterSpec = new IvParameterSpec(passwordKey);
        try {
            mCipher = Cipher.getInstance(AES_TRANSFORMATION);

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm " + AES_ALGORITHM, e);
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, "No such padding PKCS5", e);
        }


    }

    private static byte[] encodeDigest(String text) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(MESSAGEDIGEST_ALGORITHM);
            Log.i("bytesarrayencypt", digest.digest(text.getBytes()).length + "");
            return digest.digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm " + MESSAGEDIGEST_ALGORITHM, e);
        }

        return null;
    }

    public static void saveFile(Context mContext, byte[] stringToSave, String encryptedFileName) {
        ENCRYPTED_FILE_NAME = encryptedFileName;
        if (DownloadAudioFile.Checkexternalstorage() >= 0) {
            File mEncryptedFile = new File(
                    mContext.getExternalFilesDir(null)
                            + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + ENCRYPTED_FILE_NAME);
            Log.i("file", mEncryptedFile + "");

            if (SharedPrefHelper.getSharedString(mContext, Constants.DownloadKey) != "") {
                Encryption_DecryptionAudio enc = new Encryption_DecryptionAudio(SharedPrefHelper.getSharedString(mContext, Constants.DownloadKey));
                byte[] filesBytes = enc.encrypt(stringToSave);
                Log.i("filesBytes", filesBytes + "");

                try {
                    if (!mEncryptedFile.exists()) {
                        mEncryptedFile.createNewFile();
                    }

                    FileOutputStream fos = new FileOutputStream(mEncryptedFile);
                    CipherOutputStream cipherOutputStream = new CipherOutputStream(fos, mCipher);
                    cipherOutputStream.write(filesBytes);
                    cipherOutputStream.close();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    public String encryptAsBase64(byte[] clearData) {
        byte[] encryptedData = encrypt(clearData);
        Log.d("encryptdatanew", Base64.encodeBytes(encryptedData) + "");
        // encryptdatanew =Base64.encodeBytes(encryptedData);
        return Base64.encodeBytes(encryptedData);

    }

    private byte[] encrypt(byte[] clearData) {
        try {
            mCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec, mIvParameterSpec);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "Invalid key", e);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, "Invalid algorithm " + AES_ALGORITHM, e);
            return null;
        }

        byte[] encryptedData;
        try {
            encryptedData = mCipher.doFinal(clearData);
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, "Illegal block size", e);
            return null;
        } catch (BadPaddingException e) {
            Log.e(TAG, "Bad padding", e);
            return null;
        }
        return encryptedData;
    }

    public byte[] decryptFile(byte[] fileData)
            throws Exception {
        byte[] decrypted = null;
        //	aesCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        mCipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec, mIvParameterSpec);
        try {
            decrypted = mCipher.doFinal(fileData);
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, "Illegal block size", e);
            return null;
        } catch (BadPaddingException e) {
            Log.e(TAG, "Bad padding", e);
            return null;
        }
        return decrypted;
    }


}
