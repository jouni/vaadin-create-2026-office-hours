package com.example.ui;

import java.io.IOException;
import java.io.InputStream;


/**
 * Very basic ByteArrayInputStream to simulate a slow connection.
 * For testing purposes only!
 */
public class SlowByteArrayInputStream extends InputStream{

    private byte[] buf;
    private int    pos;
    private int    count;
    private int    delayMillis;

    public SlowByteArrayInputStream(byte[] buf, int delayMillis) {
        this.buf         = buf;
        this.count       = buf.length;
        this.delayMillis = delayMillis;
    }

    @Override
    public synchronized int available() {
        return count - pos;
    }

    @Override
    public int read() throws IOException {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException exc) {
            throw new RuntimeException("reading was interrupted", exc);
        }
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }
}
