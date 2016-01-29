package org.osgi.service.converter.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.osgi.service.converter.Codec;

public class JsonCodecImpl implements Codec {
    private boolean ignoreNull = true;
    private boolean pretty = false;

    @Override
    public Codec configure(Map<String, Object> m) {
        for (Map.Entry<String, Object> entry : m.entrySet()) {
            switch (entry.getKey()) {
            case "ignoreNull":
                ignoreNull = Boolean.parseBoolean(entry.getValue().toString());
                break;
            case "pretty":
                pretty = Boolean.parseBoolean(entry.getValue().toString());
                break;
            }
        }
        return this;
    }

    @Override
    public String encode(Object obj) {
        if (pretty) {
            return "{}{}{}{}" + encode(this, obj) + "{}{}{}{}";
        } else {
            return encode(this, obj);
        }
    }

    @Override
    public String encode(Codec top, Object obj) {
        if (obj == null) {
            return ignoreNull ? "" : "\"null\"";
        }

        if (obj instanceof Map) {
            return encodeMap(top, (Map) obj);
        } else if (obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof Boolean) {
            return obj.toString();
        }

        return "\"" + obj.toString() + "\"";
    }

    private String encodeMap(Codec topCodec, Map m) {
        StringBuilder sb = new StringBuilder("{");
        for (Entry<?,?> entry : (Set<Entry>) m.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null)
                if (ignoreNull)
                    continue;

            if (sb.length() > 1)
                sb.append(',');
            sb.append('"');
            sb.append(entry.getKey().toString());
            sb.append("\":");
            sb.append(topCodec.encode(topCodec, entry.getValue()));
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public <T> T decode(Codec top, Class<T> cls) {
        return null;
    }
}
