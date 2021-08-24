package com.Ghannely_Encrypt_Decrypt_Tracks;

import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;

import java.io.IOException;

/**
 * Created by Amany on 10-Oct-17.
 */

class ByteArrayDataSource implements DataSource {

    private final byte[] data;
    private int readPosition;
    private int remainingBytes;

    /**
     * @param data The data to be read.
     */
    public ByteArrayDataSource(byte[] data) {
        Assertions.checkNotNull(data);
        Assertions.checkArgument(data.length > 0);
        this.data = data;
    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        readPosition = (int) dataSpec.position;
        remainingBytes = (int) ((dataSpec.length == C.LENGTH_UNSET)
                ? (data.length - dataSpec.position) : dataSpec.length);
        if (remainingBytes <= 0 || readPosition + remainingBytes > data.length) {
            throw new IOException("Unsatisfiable range: [" + readPosition + ", " + dataSpec.length
                    + "], length: " + data.length);
        }
        return remainingBytes;
    }

    @Override
    public void close() throws IOException {
        // Do nothing.
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (remainingBytes == 0) {
            return -1;
        }
        length = Math.min(length, remainingBytes);
        System.arraycopy(data, readPosition, buffer, offset, length);
        readPosition += length;
        remainingBytes -= length;
        return length;
    }

    @Override
    public Uri getUri() {
        return null;
    }
}