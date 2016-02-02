package org.osgi.service.converter.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.CodecAdapter;
import org.osgi.service.converter.Decoding;
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
    public <T> Decoding<T> decode(Class<T> cls) {
        Decoding<T> d = delegate.decode(cls);
        return new DecodingWrapper<T>(d, cls);
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

    private class DecodingWrapper<T> implements Decoding<T> {
        private final Decoding<T> del;
        private final Class<T> clazz;

        public DecodingWrapper(Decoding<T> d, Class<T> cls) {
            del = d;
            clazz = cls;
        }

        @Override
        public T from(CharSequence in) {
            Function<String, Object> cf = fromClassRules.get(clazz);
            if (cf != null)
                return (T) cf.apply(in.toString());
            return del.from(in);
        }
    }

    private class EncodingWrapper implements Encoding {
        private final Encoding del;
        private final Object object;

        EncodingWrapper(Encoding encoding, Object obj) {
            del = encoding;
            object = obj;
        }

        @Override
        public void to(OutputStream os) throws IOException {
            os.write(getString().getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String getString() {
            if (object == null)
                return del.getString();

            Function<Object, String> cf = toClassRules.get(object.getClass());
            if (cf != null)
                return cf.apply(object);
            return del.getString();
        }
    }
}
