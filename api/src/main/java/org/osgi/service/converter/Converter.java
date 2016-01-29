package org.osgi.service.converter;

public interface Converter {
    CodecAdapter getCodecAdapter(Codec codec);

    Codec getDefaultCodec();

    Converting convert(Object obj);
}
