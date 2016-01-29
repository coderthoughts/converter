package org.osgi.service.converter.impl;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.CodecAdapter;
import org.osgi.service.converter.Converter;
import org.osgi.service.converter.Converting;

public class ConverterImpl implements Converter {
    private final Codec codec;

    private static Codec getInternalCodec() {
        return new InternalCodec();
    }

    public ConverterImpl() {
        this(getInternalCodec());
    }

    private ConverterImpl(Codec c) {
        codec = c;
    }

    @Override
    public CodecAdapter getCodecAdapter(Codec codec) {
        return new CodecAdapterImpl(codec);
    }

    @Override
    public Codec getDefaultCodec() {
        return getInternalCodec();
    }

    @Override
    public Converting convert(Object obj) {
        return new ConvertingImpl(codec, obj);
    }

    @Override
    public Converter with(Codec myCodec) {
        return new ConverterImpl(myCodec);
    }
}
