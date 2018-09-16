package org.kizombadev.eventstudio.common.elasticsearch;

import org.junit.Assert;
import org.junit.Test;

public class ElasticsearchExceptionTest {

    @Test
    public void testWithString() {
        Exception exception = new ElasticsearchException("Foo");
        Assert.assertEquals("Foo", exception.getMessage());
    }

    @Test
    public void testWithThrowable() {
        IllegalStateException stateException = new IllegalStateException("Foo");
        Exception exception = new ElasticsearchException(stateException);
        Assert.assertEquals("java.lang.IllegalStateException: Foo", exception.getMessage());
    }
}