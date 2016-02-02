package org.osgi.service.converter.impl;

import org.osgi.service.converter.Adapter;
import org.osgi.service.converter.Converter;
import org.osgi.service.converter.Converting;

public class ConverterImpl implements Converter {
    @Override
    public Adapter getAdapter() {
        return new AdapterImpl(this);
    }

    @Override
    public Converting convert(Object obj) {
        return new ConvertingImpl(this, obj);
    }
}
