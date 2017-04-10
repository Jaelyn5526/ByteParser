package byteutil.jaelynbtyeutil;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zaric on 17-04-07.
 */

public class JByteUtil {

    public static int starIndex = 0;
    public static int num = 0;

    public static Object parserByte(Class<?> cls, byte[] data) {
        starIndex = 0;
        num = 0;
        ArrayList<Field> fields = extractFields(cls);
        return parserByte(cls, data, fields);
    }

    /**
     * 提取需要解析的成员变量
     */
    private static ArrayList<Field> extractFields(Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();
        ArrayList<Field> arrayFields = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(JByte.class)) {
                arrayFields.add(fields[i]);
            }
            Log.d("field", fields[i].getName() + "---" + fields[i].getType());
        }
        Log.d("field -----", "------------");
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

        for (int i = 0; i < arrayFields.size(); i++) {
            Log.d("field", arrayFields.get(i).getName() + "---" + arrayFields.get(i).getType());
        }
        return arrayFields;
    }

    private static Object parserByte(Class<?> cls, byte[] data, ArrayList<Field> fields) {

        try {
            Object obj = Class.forName(cls.getName()).newInstance();
            for (Field field : fields) {
                if (field.getType().isPrimitive()) {
                    getPrimitiveLenght(obj, field, data);
                } else {
                    getUnPrimitiveLenght(obj, field, data);
                }
            }
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取基础类型数据的字节数
     *
     * @param field
     * @return
     */
    private static void getPrimitiveLenght(Object obj, Field field, byte[] data) {
        try {

            if (field.getType() == byte.class) {
                num = 1;
                field.setByte(obj, data[starIndex]);
                starIndex += 1;
            } else if (field.getType() == char.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 2);
                starIndex += 2;
                num = 2;
                field.setChar(obj, ByteUtil.getChar(bytes));
            } else if (field.getType() == short.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 2);
                starIndex += 2;
                num = 2;
                field.setShort(obj, ByteUtil.getShort(bytes));
            } else if (field.getType() == int.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 4);
                starIndex += 4;
                num = 4;
                field.setInt(obj, ByteUtil.getInt(bytes));
            } else if (field.getType() == long.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 8);
                starIndex += 8;
                num = 8;
                field.setLong(obj, ByteUtil.getLong(bytes));
            } else if (field.getType() == float.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 4);
                starIndex += 4;
                num = 4;
                field.setFloat(obj, ByteUtil.getFloat(bytes));
            } else if (field.getType() == double.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 8);
                starIndex += 8;
                num = 8;
                field.setDouble(obj, ByteUtil.getDouble(bytes));
            } else if (field.getType() == String.class) {
                int lenght = field.getAnnotation(JByte.class).lenght();
                byte[] bytes = new byte[lenght];
                System.arraycopy(data, starIndex, bytes, 0, lenght);
                String string = new String(bytes, "UTF-8");
                field.set(obj, string);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取基础类型数据的字节数
     *
     * @param field
     * @return
     */
    private static void getUnPrimitiveLenght(Object obj, Field field, byte[] data) {
        try {

            if (field.getType() == String.class) {
                int stringSize = 0;
                int lenght = field.getAnnotation(JByte.class).lenght();
                for (int i = starIndex; i < starIndex + lenght; i++) {
                    if (data[i] == 0){
                        stringSize = i - starIndex;
                        break;
                    }
                }
                byte[] bytes = new byte[stringSize];
                System.arraycopy(data, starIndex, bytes, 0, stringSize);
                String string = new String(bytes, "UTF-8");
                field.set(obj, string);
                starIndex += lenght;
                num = lenght;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static byte[] objectToByte(Object object) {
        Class cls = object.getClass();
        ArrayList<Field> fields = extractFields(cls);
        return objectToByte(object, fields);
    }

    private static byte[] objectToByte(Object obj, ArrayList<Field> fields) {
        byte[] bytes = new byte[0];
        byte[] data = new byte[0];
        byte[] current;
        for (Field field : fields) {
            if (field.getType().isPrimitive()) {
                current = getPrimitiveByte(field, obj);
            } else {
                current = getUnPrimitiveByte(field, obj);
            }
            bytes = Arrays.copyOf(data, data.length + current.length);
            System.arraycopy(current, 0, bytes, data.length, current.length);
            data = bytes;
        }
        return bytes;
    }

    private static byte[] getPrimitiveByte(Field field, Object obj) {
        try {
            if (field.getType() == byte.class) {
                byte[] datas = new byte[1];
                datas[0] = field.getByte(obj);
                return datas;
            } else if (field.getType() == char.class) {
                char data = field.getChar(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == short.class) {
                short data = field.getShort(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == int.class) {
                int data = field.getInt(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == long.class) {
                long data = field.getLong(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == float.class) {
                float data = field.getFloat(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == double.class) {
                double data = field.getDouble(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                byte data = field.getBoolean(obj) ? (byte) 1 : (byte) 0;
                return new byte[]{data};
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getUnPrimitiveByte(Field field, Object obj) {
        try {
            if (field.getType() == String.class) {
                byte[] data = new byte[field.getAnnotation(JByte.class).lenght()];
                byte[] strings = ((String) field.get(obj)).getBytes();
                System.arraycopy(strings, 0, data, 0, strings.length);
                return data;
            } else if (field.getType() == List.class) {

            } else if (field.getType() == Arrays.class) {

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}