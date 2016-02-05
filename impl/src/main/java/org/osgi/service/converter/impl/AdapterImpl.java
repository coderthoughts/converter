package org.osgi.service.converter.impl;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.osgi.service.converter.Adapter;
import org.osgi.service.converter.Converter;
import org.osgi.service.converter.Converting;

public class AdapterImpl implements Adapter {
    private final Converter delegate;
    private final Map<ClassPair, Function<Object, Object>> classRules =
            new ConcurrentHashMap<>();

    public AdapterImpl(Converter converter) {
        this.delegate = converter;
    }

    @Override
    public Converting convert(Object obj) {
        Converting converting = delegate.convert(obj);
        return new ConvertingWrapper(obj, converting);
    }

    @Override
    public Adapter getAdapter() {
        return new AdapterImpl(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F, T> Adapter rule(Class<F> fromCls, Class<T> toCls,
            Function<F, T> toFun, Function<T, F> fromFun) {
        if (fromCls.equals(toCls))
            throw new IllegalArgumentException();

        classRules.put(new ClassPair(fromCls, toCls), (Function<Object, Object>) toFun);
        classRules.put(new ClassPair(toCls, fromCls), (Function<Object, Object>) fromFun);
        return this;
    }

    private class ConvertingWrapper implements Converting {
        private final Converting del;
        private final Object object;

        ConvertingWrapper(Object obj, Converting c) {
            object = obj;
            del = c;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T to(Class<T> cls) {
            Function<Object, Object> f = classRules.get(new ClassPair(object.getClass(), cls));
            if (f != null)
                return (T) f.apply(object);

            return del.to(cls);
        }
    }

    static class ClassPair {
        private final Class<?> from;
        private final Class<?> to;

        ClassPair(Class<?> from, Class<?> to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof ClassPair))
                return false;

            ClassPair o = (ClassPair) obj;
            return Objects.equals(from, o.from) &&
                    Objects.equals(to, o.to);
        }
    }
}
