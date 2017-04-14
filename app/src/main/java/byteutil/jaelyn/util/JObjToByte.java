package byteutil.jaelyn.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import byteutil.jaelyn.util.ByteUtil;
import byteutil.jaelyn.util.JByte;

/**
 * Created by zaric on 17-04-11.
 */

public class JObjToByte {

    public static int num = 0;

    public static byte[] getBytes(Object object) {
        byte[] datas = variableToBytes(object);
        if (datas != null) {
            return datas;
        } else {
            return objToBytes(object);
        }
    }

    private static byte[] variableToBytes(Object obj) {
        /*if (!obj.getClass().isPrimitive() & !Number.class.isAssignableFrom(obj.getClass()) &
                obj.getClass() != String.class) {
            return null;
        }*/
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == byte.class | obj.getClass() == Byte.class) {
            num = 1;
            return new byte[]{(byte) obj};
        } else if (obj.getClass() == char.class | obj.getClass() == Character.class) {
            num = 2;
            return ByteUtil.getBytes((char) obj);
        } else if (obj.getClass() == short.class | obj.getClass() == Short.class) {
            num = 2;
            return ByteUtil.getBytes((short) obj);
        } else if (obj.getClass() == int.class | obj.getClass() == Integer.class) {
            num = 4;
            return ByteUtil.getBytes((int) obj);
        } else if (obj.getClass() == long.class | obj.getClass() == Long.class) {
            num = 8;
            return ByteUtil.getBytes((long) obj);
        } else if (obj.getClass() == float.class | obj.getClass() == Float.class) {
            num = 4;
            return ByteUtil.getBytes((float) obj);
        } else if (obj.getClass() == double.class | obj.getClass() == Double.class) {
            num = 8;
            return ByteUtil.getBytes((double) obj);
        } else if (obj.getClass() == boolean.class || obj.getClass() == Boolean.class) {
            num = 1;
            byte data = (boolean) obj ? (byte) 1 : (byte) 0;
            return new byte[]{data};
        } else if (obj.getClass() == String.class) {
            num = 0;
            return ByteUtil.getBytes((String) obj);
        }
        return null;
    }

    private static byte[] objToBytes(Object obj) {
        if (obj == null){
            return null;
        }
        if (List.class.isAssignableFrom(obj.getClass())) {  //解析链表
            List<Object> objs = (List<Object>) obj;
            List<Byte> listData = new ArrayList<>();
            for (int i = 0; i < objs.size(); i++) {
                byte[] childByte = getBytes(objs.get(i));
                for (byte child : childByte){
                    listData.add(child);
                }
            }
            byte[] datas = new byte[listData.size()];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = listData.get(i);
            }
            return datas;
        } else if (obj.getClass().isArray()) { // 解析数组
            int lenght = Array.getLength(obj);
            List<Byte> listData = new ArrayList<>();
            for (int i = 0; i < lenght; i++) {
                Object childObj = Array.get(obj, i);
                byte[] childByte = getBytes(childObj);
                for (byte child : childByte){
                    listData.add(child);
                }
            }
            byte[] datas = new byte[listData.size()];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = listData.get(i);
            }
            return datas;
        } else {  //解析对象
            try {
                List<Field> fields = extractFields(obj.getClass());
                List<Byte> listData = new ArrayList<>();
                for (Field field : fields) {
                    Object childObj = field.get(obj);
                    byte[] childByte = getBytes(childObj);
                    int lenght = field.getAnnotation(JByte.class).lenght();
                    if (lenght != 0 && num != 0){
                        int childlgt = childByte.length;
                        for (int i = 0; i < lenght * num; i++) {
                            if (i < childlgt) {
                                listData.add(childByte[i]);
                            } else {
                                listData.add((byte) 0);
                            }
                        }
                    }else {
                        for (byte child : childByte){
                            listData.add(child);
                        }
                    }

                }
                byte[] datas = new byte[listData.size()];
                for (int i = 0; i < datas.length; i++) {
                    datas[i] = listData.get(i);
                }
                return datas;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 提取需要解析的成员变量
     */
    private static List<Field> extractFields(Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> arrayFields = new ArrayList<>();
        if (List.class.isAssignableFrom(cls) | cls.isArray()) {
            arrayFields = Arrays.asList(fields);
        } else {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isAnnotationPresent(JByte.class)) {
                    arrayFields.add(fields[i]);
                }
            }
        }
        Collections.sort(arrayFields, new Comparator<Field>() {
            @Override
            public int compare(Field field1, Field field2) {
                if (!field1.isAnnotationPresent(JByte.class)) {
                    return -1;
                } else if (!field2.isAnnotationPresent(JByte.class)) {
                    return 0;
                } else {
                    JByte jbyte1 = field1.getAnnotation(JByte.class);
                    JByte jbyte2 = field2.getAnnotation(JByte.class);
                    return jbyte1.index() < jbyte2.index() ? -1 : 0;
                }
            }
        });
        return arrayFields;
    }
}
