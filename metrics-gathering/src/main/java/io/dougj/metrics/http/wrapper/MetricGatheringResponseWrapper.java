package io.dougj.metrics.http.wrapper;

import io.dougj.metrics.http.HttpUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

public class MetricGatheringResponseWrapper extends HttpServletResponseWrapper {

    private final HttpServletResponse response;
    private final MetricGatheringServletOutputStream countingOutputStream;
    private final PrintWriter writer;

    private Instant processingStartTime;

    public MetricGatheringResponseWrapper(HttpServletResponse response,
                                          String uniqueRequestIdentifier) throws IOException {
        super(response);
        this.response = response;
        this.countingOutputStream = new MetricGatheringServletOutputStream(response.getOutputStream());
        OutputStreamWriter streamWriter = new OutputStreamWriter(countingOutputStream, StandardCharsets.UTF_8);
        this.writer = new PrintWriter(streamWriter);
        this.setHeader(HttpUtil.REQUEST_ID_HEADER, uniqueRequestIdentifier);    // set unique identifier in response header
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return countingOutputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public void flushBuffer() {
        writer.flush();
    }

    /**
     * start time of request to track processing time
     *
     * @return time when request processing began by filter
     */
    public Instant getProcessingStartTime() {
        return processingStartTime;
    }


    /**
     * Set request processing start time
     * @param processingStartTime - Instant
     */
    public void setProcessingStartTime(Instant processingStartTime) {
        this.processingStartTime = processingStartTime;
    }

    /**
     * Returns the number of bytes written to the ServletOutputStream
     *
     * @return number of bytes written to the response
     */
    public long getResponseByteCount() throws IOException {
        flushBuffer();
        return countingOutputStream.getByteCount();
    }

    /**
     * Calculate request time start to finish
     *
     * @return total request processing time
     */
    public long getProcessingTime() {
        return Duration.between(processingStartTime, Instant.now()).toMillis();
    }
}
