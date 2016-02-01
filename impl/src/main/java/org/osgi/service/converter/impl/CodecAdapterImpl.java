package org.osgi.service.converter.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.CodecAdapter;
import org.osgi.service.converter.Encoding;

public class CodecAdapterImpl implements CodecAdapter {
    private final Codec delegate;
    private final Map<Class<Object>, Function<Object, String>> classRules = new ConcurrentHashMap<>();
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
    public Codec from(Codec codec) {
        topCodec = codec;
        return this;
    }

    @Override
    public Encoding encode(Object obj) {
        Encoding e = delegate.from(topCodec).encode(obj);
        return new EncodingWrapper(e, obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CodecAdapter rule(Class<T> cls, Function<T, String> f) {
        classRules.put((Class<Object>) cls, (Function<Object, String>) f);
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
        public String getString() {
            if (object == null)
                return delegate.getString();

            Function<Object, String> cf = classRules.get(object.getClass());
            if (cf != null)
                return cf.apply(object);
            return delegate.getString();
        }
    }
}
