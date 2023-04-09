package com.fsc.async;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.PullResponseItem;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class FuturePullImageResultCallback implements ResultCallback<PullResponseItem> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private PullResponseItem result;
    private Throwable error;

    @Override
    public void onStart(Closeable closeable) {
        // 不需要实现
    }

    @Override
    public void onNext(PullResponseItem item) {
        // 不需要实现
    }

    @Override
    public void onError(Throwable throwable) {
        error = throwable;
        latch.countDown();
    }

    @Override
    public void onComplete() {
        latch.countDown();
    }

    public PullResponseItem awaitResult() throws InterruptedException {
        latch.await();
        if (error != null) {
            throw new RuntimeException(error);
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        // 不需要实现
    }
}
