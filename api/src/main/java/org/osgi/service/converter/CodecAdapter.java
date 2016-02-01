package org.osgi.service.converter;

import java.util.function.Function;

public interface CodecAdapter extends Codec {
    <T> CodecAdapter rule(Class<T> cls, Function<T, String> toString, Function<String, T> fromString);
}
