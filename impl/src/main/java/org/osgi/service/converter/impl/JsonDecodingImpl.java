package org.osgi.service.converter.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.converter.Decoding;

public class JsonDecodingImpl<T> implements Decoding<T> {
    private final Class<T> clazz;

    public JsonDecodingImpl(Class<T> cls) {
        clazz = cls;
    }

    @Override
    public T from(CharSequence in) {
        if (Map.class.isAssignableFrom(clazz)) {
            return createMapFromJSONString(in);
        }
        return tryStandardMethods(clazz, in);
    }

    private T createMapFromJSONString(CharSequence in) {
        Map m = new HashMap();
        String s = in.toString().trim();
        if (!s.startsWith("{") || !s.endsWith("}"))
            throw new IllegalArgumentException("JSON Should start and end with '{' and '}': " + s);

        // Eat braces
        s = s.substring(1, s.length() - 1);

        int commaIdx = -1;
        do {
            int colonIdx = s.indexOf(':');
            if (colonIdx <= 0)
                throw new IllegalArgumentException("JSON Should contain key-value pairs: " + s);

            String key = s.substring(0, colonIdx).trim();
            if (!key.startsWith("\"") || !key.endsWith("\""))
                throw new IllegalArgumentException("JSON key should be double-quoted: " + s);
            key = key.substring(1, key.length() - 1);

            // move to after ':'
            s = s.substring(colonIdx + 1);

            commaIdx = getNextComma(s);
            String val;
            if (commaIdx > 0) {
                val = s.substring(0, commaIdx);

                // move to after ','
                s = s.substring(commaIdx + 1);
            } else {
                val = s;
            }


            val = val.trim();
            Object parsed;
            if (val.startsWith("{")) {
                parsed = new JsonCodecImpl().decode(Map.class).from(val);
            } else {
                parsed = val; // TODO?
            }
            m.put(key, parsed);
        } while (commaIdx > 0);

        return (T) m;
    }

    private int getNextComma(String s) {
        int bracelevel = 0;
        for (int i=0; i<s.length(); i++) {
            switch(s.charAt(i)) {
            case '{': bracelevel++;
                break;
            case '}': bracelevel--;
                break;
            case ',': if (bracelevel == 0) return i;
                break;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private <T> T tryStandardMethods(Class<T> cls, CharSequence cs) {
        try {
            Method m = cls.getDeclaredMethod("valueOf", String.class);
            if (m != null) {
                return (T) m.invoke(null, cs);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
