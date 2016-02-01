package org.osgi.service.converter;

import java.io.IOException;
import java.io.OutputStream;

public interface Encoding {
    void to(OutputStream os) throws IOException;
    String getString();
}
