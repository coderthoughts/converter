package org.osgi.service.converter.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.CodecAdapter;
import org.osgi.service.converter.Encoding;

public class CodecAdapterImpl implements CodecAdapter {
    private final Codec delegate;
    private final Map<Class<Object>, Function<Object, String>> toClassRules =
            new ConcurrentHashMap<>();
    private final Map<Class<Object>, Function<String, Object>> fromClassRules =
            new ConcurrentHashMap<>();
    private Codec topCodec = this;

    public CodecAdapterImpl(Codec codec) {
        this.delegate = codec;
    }

    @Override
    public Codec configure(Map<String, Object> m) {
        delegate.configure(m);
        return this;
    }

    @Override
    public Codec configure(String key, Object val) {
        delegate.configure(key, val);
        return this;
    }

    @Override
    public Codec with(Codec codec) {
        topCodec = codec;
        return this;
    }

    @Override
    public Encoding encode(Object obj) {
        Encoding e = delegate.with(topCodec).encode(obj);
        return new EncodingWrapper(e, obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CodecAdapter rule(Class<T> cls, Function<T, String> toClass, Function<String, T> fromClass) {
        toClassRules.put((Class<Object>) cls, (Function<Object, String>) toClass);
        fromClassRules.put((Class<Object>) cls, (Function<String, Object>) fromClass);
        return this;
    }

    private class EncodingWrapper implements Encoding {
        private final Encoding delegate;
        private final Object object;

        EncodingWrapper(Encoding encoding, Object obj) {
            delegate = encoding;
            object = obj;
        }

        @Override
        public void to(OutputStream os) throws IOException {
            os.write(getString().getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String getString() {
            if (object == null)
                return delegate.getString();

            Function<Object, String> cf = toClassRules.get(object.getClass());
            if (cf != null)
                return cf.apply(object);
            return delegate.getString();
        }
    }
}
