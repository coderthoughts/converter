package org.osgi.service.converter.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.Converter;
import org.osgi.service.converter.Decoding;
import org.osgi.service.converter.Encoding;

public class JsonCodecImpl implements Codec {
    private Map<String, Object> configuration = new ConcurrentHashMap<>();
    private ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();
    private Converter converter = new ConverterImpl(); // TODO inject?

    @Override
    public Codec configure(Map<String, Object> m) {
        configuration.putAll(m);
        return this;
    }

    @Override
    public Codec with(Converter c) {
        converter = c;
        return this;
    }

    @Override
    public <T> Decoding<T> decode(Class<T> cls) {
        return new JsonDecodingImpl<T>(converter, cls);
    }

    @Override
    public Encoding encode(Object obj) {
        Encoding encoding = new JsonEncodingImpl(converter, configuration, obj);

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
