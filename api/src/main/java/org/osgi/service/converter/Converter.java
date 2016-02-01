package org.osgi.service.converter;

public interface Converter {
    Converting convert(Object obj);

    CodecAdapter getCodecAdapter(Codec codec);

    Codec getDefaultCodec();
}
