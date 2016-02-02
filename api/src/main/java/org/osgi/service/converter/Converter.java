package org.osgi.service.converter;

public interface Converter {
    Converting convert(Object obj);

    Adapter getAdapter();
}
