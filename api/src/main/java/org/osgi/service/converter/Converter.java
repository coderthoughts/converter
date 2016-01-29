package org.osgi.service.converter;

public interface Converter {
    Codec defaultCodec();

    Converting convert(Object obj);

    Converter with(Codec myCodec);
}
