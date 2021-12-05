package com.mile.portal.adaptor;

import com.mile.portal.config.exception.exceptions.RemoteServiceNotAvailableException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 * Adaptor (외부연동) 시에 상속받아서 구현
 */
public interface AdapterRetry {
    @Retryable(value = {RemoteServiceNotAvailableException.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    String adapterProcess();

    @Recover
    String adapterProcessFallback(RuntimeException e);
}
