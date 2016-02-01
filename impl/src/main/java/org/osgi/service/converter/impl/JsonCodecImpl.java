package org.osgi.service.converter.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.Encoding;

public class JsonCodecImpl implements Codec {
    private Map<String, Object> configuration = new ConcurrentHashMap<>();
    private Codec parentCodec = this;
    private ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();

    @Override
    public Codec configure(Map<String, Object> m) {
        configuration.putAll(m);
        return this;
    }

    @Override
    public Codec with(Codec codec) {
        parentCodec = codec;
        return this;
    }

    @Override
    public Encoding encode(Object obj) {
        Encoding encoding = new JsonEncodingImpl(parentCodec, configuration, obj);

        if (pretty()) {
            Boolean top = threadLocal.get();
            if (top == null) {
                threadLocal.set(Boolean.TRUE);

                encoding = new EncodingWrapper("{}{}{}{}{}", encoding, "{}{}{}{}{}");
            }
        }
        return encoding;
    }

    private boolean pretty() {
        return Boolean.TRUE.equals(Boolean.parseBoolean((String) configuration.get("pretty")));
    }

    private class EncodingWrapper implements Encoding {
        private final Encoding delegate;
        private String prefix;
        private String postfix;

        EncodingWrapper(String pre, Encoding encoding, String post) {
            prefix = pre;
            delegate = encoding;
            postfix = post;
        }

        @Override
        public void to(OutputStream os) throws IOException {
            os.write(getString().getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String getString() {
            try {
                return prefix + delegate.getString() + postfix;
            } finally {
                threadLocal.set(null);
            }
        }
    }
}
