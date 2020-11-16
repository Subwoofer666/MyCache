import java.io.*;
import java.util.Map;

public class Main implements Serializable {
    public static void main(String[] args) throws Exception {
        MyCache<String, String> zxc = new MyCache<>(2000);
        zxc.put("asd", "aaaaaaaaa");
        zxc.put("asd2", "cccccccc");
        zxc.put("asd3", "bbbbbbbbbb");

        System.out.println(zxc.get("asd"));
        System.out.println(zxc.get("asd2"));
        System.out.println(zxc.get("asd3"));
        Thread.sleep(500);

        MyCache.Serializable(zxc, "qwe");
        MyCache.Deserializable("qwe");

        System.out.println(zxc.get("asd"));
        System.out.println(zxc.get("asd2"));
        System.out.println(zxc.get("asd3"));
        Thread.sleep(4000);
        System.out.println(zxc.get("asd"));
        System.out.println(zxc.get("asd2"));
        System.out.println(zxc.get("asd3"));

        MyCache.Serializable(zxc, "qwe");
        MyCache.Deserializable("qwe");
        }
    }

