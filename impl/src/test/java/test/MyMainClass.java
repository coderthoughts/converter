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
//        ca0.rule(Long.class, v -> "\"" + v + "elf" + v + "\"");
        ca0.rule(String[].class, Arrays::toString, v -> v.split(","));
        String[] sa = c.convert(new String[] {"A", "B"}).to(String[].class);
        System.out.println("Without CA0:" + Arrays.toString(sa));
        String[] sa2 = c.convert(new String[] {"A", "B"}).with(ca0).to(String[].class);
        System.out.println("With CA0:" + Arrays.toString(sa2));
        // decode as well... via Converter
//        System.out.println("2: Map -> String " + c.convert(11L).with(ca0).to(String.class));
//        System.out.println("2: Map -> String " + c.convert(12L).with(ca0).to(String.class));
//        System.out.println("2: Map -> String " + c.convert(11L).to(String.class));

        // use 1
        JsonCodecImpl jsonCodec = new JsonCodecImpl();
        System.out.println("U1: " + c.convert(m).with(jsonCodec).to(String.class));
        // use 2
        jsonCodec.configure("pretty", "true");
//        System.out.println("U2: " + jsonCodec.encode(m).getString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            jsonCodec.encode(m).to(baos);
        } finally {
            baos.close();
        }
        System.out.println("U2: " + new String(baos.toByteArray()));

        CodecAdapter ca = c.getCodecAdapter(jsonCodec);
//        ca.rule(Boolean.class, v -> "XX" + v.toString().substring(0, 1) + "XX");
        System.out.println("CA: " + ca.encode(m).getString());

        /*
        JsonCodecImpl jc = new JsonCodecImpl();
        System.out.println("mn: " + jc.encode(m));
        System.out.println("mP: " + jc.configure("pretty", "true").encode(m));
        System.out.println("mN: " + jc.configure("ignoreNull", "false").encode(m));

        CodecAdapter ca = new CodecAdapterImpl(jc);
        ca.valueRule(11L, v -> "\"eleven\"");
        ca.classRule(Boolean.class, v -> "XX" + v.toString().substring(0, 1) + "XX");
        System.out.println("Ca: " + ca.encode(m));

//        ca.rule(Boolean.class, lambda);

        /*
        ConverterImpl converter = new ConverterImpl();

        Codec myCodec = new MyCodec(converter.defaultCodec());

        System.out.println("R1: " + converter.with(myCodec).convert(12L).to(String.class));
        System.out.println("R2: " + converter.with(myCodec).convert(13L).to(String.class));
        System.out.println("R3: " + converter.with(myCodec).convert(null).to(String.class));
        System.out.println("R4: " + converter.with(myCodec).convert(null).to(String.class)); // Maybe we should return a Codec that is configured?

        System.out.println("Result: " + new CodecImpl().encode(12L).getString());
        */
//        Runnable r = new EncoderImpl().decode(Runnable.class).ignoreNull().from("");
//        System.out.println("***" + r);
    }
/*
    private static class MyCodec implements Codec {
        private final Codec delegate;

        public MyCodec(Codec del) {
            delegate = del;
        }

        @Override
        public Encoding encode(Object obj) {
            return new MyEncodingImpl(obj, delegate);
        }

        @Override
        public <T> Decoding<T> decode(Class<T> cls) {
            // TODO Auto-generated method stub
            return null;
        }
    }


    private static class MyEncodingImpl implements Encoding {
        // Support specific configuration?

        private final Object object;
        private final Codec delegate;

        public MyEncodingImpl(Object obj, Codec del) {
            object = obj;
            delegate = del;
        }

        @Override
        public Encoding ignoreNull() {
            return this;
        }

        @Override
        public String getString() {
            if (Long.valueOf(13L).equals(object)) {
                return "14";
            } else {
                return delegate.encode(object).getString();
            }
        }

    } */
}