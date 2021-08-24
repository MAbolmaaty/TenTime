package com.Ghannely_Encrypt_Decrypt_Tracks;

import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Amany on 16-Jan-18.
 */

public class Aes128DataSource implements DataSource {

    private final DataSource upstream;
    private final byte[] encryptionKey;
    private final byte[] encryptionIv;

    private CipherInputStream cipherInputStream;

    /**
     * @param upstream The upstream {@link DataSource}.
     * @param encryptionKey The encryption key.
     * @param encryptionIv The encryption initialization vector.
     */
    public Aes128DataSource(DataSource upstream, byte[] encryptionKey, byte[] encryptionIv) {
        this.upstream = upstream;
        this.encryptionKey = encryptionKey;
        this.encryptionIv = encryptionIv;
    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }

        Key cipherKey = new SecretKeySpec(encryptionKey, "AES");
        AlgorithmParameterSpec cipherIV = new IvParameterSpec(encryptionIv);

        try {
            cipher.init(Cipher.DECRYPT_MODE, cipherKey, cipherIV);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        cipherInputStream = new CipherInputStream(
                new DataSourceInputStream(upstream, dataSpec), cipher);

        return C.AUDIO_SESSION_ID_UNSET;
    }

    @Override
    public void close() throws IOException {
        cipherInputStream = null;
        upstream.close();
    }

    @Override
    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        Assertions.checkState(cipherInputStream != null);
        int bytesRead = cipherInputStream.read(buffer, offset, readLength);
        if (bytesRead < 0) {
            return -1;
        }
        return bytesRead;
    }

    @Override
    public Uri getUri() {
        return null;
    }

}