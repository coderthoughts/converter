package org.osgi.service.converter;

import java.util.function.Function;

public interface CodecAdapter extends Codec {
    <T> CodecAdapter classRule(Class<T> cls, Function<T, String> f);
    <T> CodecAdapter valueRule(T i, Function<T, String> f);
}
