package org.osgi.service.converter.impl;

import org.osgi.service.converter.Decoding;

public class DecodingImpl<T> implements Decoding<T> {
    private final Class<T> clazz;

    public DecodingImpl(Class<T> cls) {
        clazz = cls;
    }

    @Override
    public T from(CharSequence in) {
        // TODO Auto-generated method stub
        return null;
    }
}
