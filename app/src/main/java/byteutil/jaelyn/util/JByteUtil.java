package byteutil.jaelyn.util;

/**
 * Created by zaric on 17-04-07.
 */

public class JByteUtil {

    /**
     * 将byte[]解析成Object
     * @param cls
     * @param bytes
     * @return
     */
    public static Object getObject(Class<?> cls, byte... bytes){
        return JByteToObj.getObject(cls, bytes);
    }

    /**
     * 将对象转成byte[]
     * @param object
     * @return
     */
    public static byte[] getBytes(Object object){
        return JObjToByte.getBytes(object);
    }

}