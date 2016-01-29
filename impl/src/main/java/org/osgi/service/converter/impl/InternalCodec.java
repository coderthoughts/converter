package org.osgi.service.converter.impl;

import java.util.Map;

import org.osgi.service.converter.Codec;

public class InternalCodec implements Codec {
    @Override
    public Codec configure(Map<String, Object> m) {
        return this;
    }

    @Override
    public String encode(Codec top, Object obj) {
        if (obj == null)
            return null;

        if (obj instanceof Map)
            return encodeMap(top, (Map) obj);

        return obj.toString();
    }

    private String encodeMap(Codec top, Map obj) {
        // TODO Auto-generated method stub
        return null;
    }

    /*

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

     */
    @Override
    public <T> T decode(Codec top, Class<T> cls) {
        return null;
    }
}
