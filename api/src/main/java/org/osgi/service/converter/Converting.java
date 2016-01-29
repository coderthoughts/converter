package org.osgi.service.converter;

public interface Converting {
    <T> T to(Class<T> cls);
}
