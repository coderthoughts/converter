package org.osgi.service.converter;

import java.util.function.Function;

public interface Adapter extends Converter {
    <F, T> Adapter rule(Class<F> fromCls, Class<T> toCls,
            Function<F, T> toFun, Function<T, F> fromFun);
}
