package com.webank.wecross.stub.fabric2.rpc.methods;

import com.webank.wecross.stub.fabric2.exception.ErrorCode;
import com.webank.wecross.stub.fabric2.exception.FabricRPCException;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Callback<T> {
    private static Timer timer = new HashedWheelTimer();
    private static final long CALLBACK_TIMEOUT = 30000; // ms
    private Timeout timeoutWorker;
    private AtomicBoolean isFinish = new AtomicBoolean(false);

    public Callback() {
        timeoutWorker =
                timer.newTimeout(
                        timeout -> {
                            if (!isFinish.getAndSet(true)) {
                                timeoutWorker.cancel();
                                onFailed(
                                        new FabricRPCException(
                                                ErrorCode.REMOTECALL_ERROR, "Timeout"));
                            }
                        },
                        CALLBACK_TIMEOUT,
                        TimeUnit.MILLISECONDS);
    }

    public abstract void onSuccess(T response);

    public abstract void onFailed(FabricRPCException e);

    public void callOnSuccess(T response) {
        if (!isFinish.getAndSet(true)) {

            timeoutWorker.cancel();
            onSuccess(response);
        }
    }

    public void callOnFailed(FabricRPCException e) {
        if (!isFinish.getAndSet(true)) {

            timeoutWorker.cancel();
            onFailed(e);
        }
    }
}
