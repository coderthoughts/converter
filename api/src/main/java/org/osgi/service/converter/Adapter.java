package org.osgi.service.converter;

import java.util.function.Function;

public interface Adapter extends Converter {
    <T> Adapter rule(Class<T> cls, Function<T, String> toString, Function<String, T> fromString);
}
