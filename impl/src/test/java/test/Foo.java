package test;

public class Foo {
    private final String val;

    public Foo(String s) {
        val = s;
    }

    public String tsFun() {
        return "<" + val + ">";
    }

    public static Foo fsFun(String s) {
        return new Foo(s.substring(1, s.length() - 1));
    }
}
