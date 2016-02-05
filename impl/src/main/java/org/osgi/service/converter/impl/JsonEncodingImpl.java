package org.osgi.service.converter.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.osgi.service.converter.Converter;
import org.osgi.service.converter.Encoding;

public class JsonEncodingImpl implements Encoding {
    private final Converter converter;
    private final Map<String, Object> configuration;
    private final Object object;

    JsonEncodingImpl(Converter c, Map<String, Object> cfg, Object obj) {
        converter = c;
        configuration = cfg;
        object = obj;
    }

    private boolean ignoreNull() {
        return Boolean.TRUE.equals(Boolean.parseBoolean((String) configuration.get("ignoreNull")));
    }

    @Override
    public void to(OutputStream os) throws IOException {
        os.write(encode(object).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getString() {
        return encode(object);
    }

    public String encode(Object obj) {
        if (obj == null) {
            return ignoreNull() ? "" : "null";
        }

        if (obj instanceof Map) {
            return encodeMap((Map) obj);
        } else if (obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof Boolean) {
            return obj.toString();
        }

        return "\"" + converter.convert(obj).to(String.class) + "\"";
    }

    private String encodeMap(Map m) {
        StringBuilder sb = new StringBuilder("{");
        for (Entry<?,?> entry : (Set<Entry>) m.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null)
                if (ignoreNull())
                    continue;

            if (sb.length() > 1)
                sb.append(',');
            sb.append('"');
            sb.append(entry.getKey().toString());
            sb.append("\":");
            sb.append(encode(entry.getValue()));
        }
        sb.append("}");

        return sb.toString();
    }
}
