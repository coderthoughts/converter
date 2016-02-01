package org.osgi.service.converter;

public interface Decoding<T> {
    T from(CharSequence in);
}
