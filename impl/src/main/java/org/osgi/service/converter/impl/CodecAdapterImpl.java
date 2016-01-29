package org.osgi.service.converter.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.CodecAdapter;

public class CodecAdapterImpl implements CodecAdapter {
    private final Codec delegate;

    private final Map<Class<Object>, Function<Object, String>> classRules = new ConcurrentHashMap<>();
    private final Map<Object, Function<Object, String>> valueRules = new ConcurrentHashMap<>();

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
    public String encode(Object obj) {
        return encode(this, obj);
    }

    @Override
    public String encode(Codec topCodec, Object obj) {
        if (obj == null)
            return delegate.encode(topCodec, obj);

        Function<Object, String> f = valueRules.get(obj);
        if (f != null)
            return f.apply(obj);

        Function<Object, String> cf = classRules.get(obj.getClass());
        if (cf != null)
            return cf.apply(obj);
        return delegate.encode(topCodec, obj);
    }

    @Override
    public <T> T decode(Class<T> cls) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T decode(Codec topCodec, Class<T> cls) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CodecAdapterImpl valueRule(T i, Function<T, String> f) {
        valueRules.put(i, (Function<Object, String>) f); // TODO can we get rid of this cast?
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CodecAdapter classRule(Class<T> cls, Function<T, String> f) {
        classRules.put((Class<Object>) cls, (Function<Object, String>) f);
        return this;
    }
}
