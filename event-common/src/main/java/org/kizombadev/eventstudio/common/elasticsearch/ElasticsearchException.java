package org.kizombadev.eventstudio.common.elasticsearch;

public class ElasticsearchException extends RuntimeException {
    public ElasticsearchException(String message) {
        super(message);
    }

    public ElasticsearchException(Throwable cause) {
        super(cause);
    }
}
