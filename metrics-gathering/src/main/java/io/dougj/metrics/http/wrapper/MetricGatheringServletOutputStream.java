package io.dougj.metrics.http.wrapper;

import org.apache.commons.io.output.CountingOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class MetricGatheringServletOutputStream extends ServletOutputStream {

    // Apache commons IO lib with byte counting capability
    private final CountingOutputStream outputStream;

    public MetricGatheringServletOutputStream(ServletOutputStream outputStream) {
        this.outputStream = new CountingOutputStream(outputStream);
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(byte[] bts, int st, int end) throws IOException {
        outputStream.write(bts, st, end);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    public long getByteCount() {
        return outputStream.getByteCount();
    }

    @Override
    public boolean isReady() {
        // really implement me if using jakarta.servlet.ServletOutputStream
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        // implement me if using jakarta.servlet.ServletOutputStream
    }
}
