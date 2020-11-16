import java.io.*;
import java.lang.ref.SoftReference;
import java.security.Key;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.*;

@SuppressWarnings("All")
public class MyCache <K,V> implements Serializable {

    private static final long serialVersionUID = 1;

  private volatile ConcurrentHashMap MyCache = new ConcurrentHashMap();
  private long timeToLive;
  private transient ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
          Thread thread = new Thread(r);
          thread.setDaemon(true);
          return thread;
      }});

    protected class CacheObject implements Serializable {
        public long lastAccessed = System.currentTimeMillis();
        public V value;

        protected CacheObject(V value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "MyCache " + MyCache +
                ", timeToLive = " + timeToLive +
                ", scheduledExecutorService = " + scheduledExecutorService;
    }

    public MyCache(long timeToLive) throws Exception {
        this.timeToLive = timeToLive;
        if (timeToLive < 10) {
            throw new Exception("Time to live не может быть меньше 10мс");
        }
                scheduledExecutorService.scheduleAtFixedRate(() -> {
            long current = System.currentTimeMillis();
            for (Object k : MyCache.keySet()) {
                MyCache.remove(k);
            }
        }, timeToLive, timeToLive/5, TimeUnit.MILLISECONDS);

    }


  public Object get(K key) {
      CacheObject c = (CacheObject) MyCache.get(key);
      if (c == null) return null;

      return c.value;
  }

  public void put(Object key, Object value) {
      MyCache.put(key, new CacheObject((V) value));

  }

  public void remove(Object key) {
      MyCache.remove(key);
  }

    public int size() {
            return MyCache.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyCache<?, ?> myCache = (MyCache<?, ?>) o;
        return Objects.equals(MyCache, myCache.MyCache) &&
                Objects.equals(scheduledExecutorService, myCache.scheduledExecutorService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MyCache, scheduledExecutorService);
    }

    public static void Serializable(Object o, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(o);
        oos.flush();
        oos.close();
    }
    public static void Deserializable (String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        System.out.println(ois.readObject());
        ois.close();
    }
}
