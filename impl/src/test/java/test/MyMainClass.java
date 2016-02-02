package test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.converter.CodecAdapter;
import org.osgi.service.converter.Converter;
import org.osgi.service.converter.impl.ConverterImpl;
import org.osgi.service.converter.impl.JsonCodecImpl;

public class MyMainClass {

    public static void main(String [] args) throws Exception {
        String[] a = {"A", "B"};
        System.out.println("" + Arrays.stream(a).collect(Collectors.joining(",")));
        System.out.println("" + Stream.of(a).collect(Collectors.joining(":")));

        Map<Object, Object> m1 = new HashMap<>();
        m1.put("x", true);
        m1.put("y", null);
        Map<Object, Object> m = new HashMap<>();
        m.put(1, 11L);
        m.put("ab", "cd");
        m.put(true, m1);

        Converter c = new ConverterImpl();
        System.out.println("Long -> String " + c.convert(12L).to(String.class));
        System.out.println("String -> Long " + c.convert("123").to(Long.class));
        System.out.println("Map -> String " + c.convert(m).to(String.class));

        CodecAdapter ca0 = c.getCodecAdapter(c.getDefaultCodec());
        ca0.rule(String[].class,
                v -> Stream.of(v).collect(Collectors.joining(",")),
                v -> v.split(","));
        String sa = c.convert(new String[] {"A", "B"}).to(String.class);
        System.out.println("Without CA0:" + sa);
        String sa2 = c.convert(new String[] {"A", "B"}).with(ca0).to(String.class);
        System.out.println("With CA0:" + sa2);

        String[] decoded = c.convert(sa2).to(String[].class);
        System.out.println("decoded: " + Arrays.toString(decoded) + " len: " + decoded.length);
        String[] decoded2 = c.convert(sa2).with(ca0).to(String[].class);
        System.out.println("decoded2: " + Arrays.toString(decoded2) + " len: " + decoded2.length);

        // use 1
        JsonCodecImpl jsonCodec = new JsonCodecImpl();
        System.out.println("U1: " + c.convert(m).with(jsonCodec).to(String.class));
        // use 2
        jsonCodec.configure("pretty", "true");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            jsonCodec.encode(m).to(baos);
        } finally {
            baos.close();
        }
        System.out.println("U2: " + new String(baos.toByteArray()));
        String ms = new JsonCodecImpl().encode(m).getString();
        System.out.println("U3: " + ms);
        Map<?,?> m2 = new JsonCodecImpl().decode(Map.class).from(ms);
        System.out.println("M2: " + m2);

        CodecAdapter ca = c.getCodecAdapter(jsonCodec);
        ca.rule(Boolean.class,
                v -> "XX" + v.toString().charAt(0) + "XX",
                v -> v.charAt(2)=='t'?true:false);
        System.out.println("CA: " + ca.encode(m).getString());
        String bs = ca.encode(true).getString();
        boolean b = ca.decode(Boolean.class).from(bs);
        System.out.println("" + bs + "=" + b);
        String bs2 = ca.encode(false).getString();
        boolean b2 = ca.decode(Boolean.class).from(bs2);
        System.out.println("" + bs2 + "=" + b2);
    }
}