package org.osgi.service.converter.impl;

import java.util.Map;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.Decoding;
import org.osgi.service.converter.Encoding;

public class InternalCodec implements Codec {
    @Override
    public Codec configure(Map<String, Object> m) {
        return this;
    }

    @Override
    public <T> Decoding<T> decode(Class<T> cls) {
        return new DecodingImpl<T>(cls);
    }

    @Override
    public Encoding encode(Object obj) {
        return new EncodingImpl(obj);
    }

    @Override
    public Codec with(Codec parentCodec) {
        return this;
    }
}
