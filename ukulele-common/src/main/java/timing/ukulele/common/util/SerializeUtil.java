package timing.ukulele.common.util;

import java.io.*;

/**
 * 序列化辅助类
 */
public final class SerializeUtil {
    private SerializeUtil() {
    }

    /**
     * 序列化
     *
     * @param object 要处理的对象
     * @return 字节数组
     */
    public static byte[] serialize(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 要反序列化的字节数组
     * @return 对象
     */
    public static final Object deserialize(byte[] bytes) {
        return deserialize(bytes, Object.class);
    }

    /**
     * 反序列化
     *
     * @param bytes 要反序列化的字节数组
     * @param cls   反序列类型
     * @param <K>   K
     * @return 反序列化后的对象
     */
    @SuppressWarnings("unchecked")
    public static <K> K deserialize(byte[] bytes, Class<K> cls) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            return (K) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bais.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
