package byteutil.jaelynbtyeutil;

import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by zaric on 17-04-07.
 */

public class JByteParser {

    public static int starIndex = 0;
    public static int num = 0;

    public static Object parserByte(Class<?> cls, byte[] data) {
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
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).isAnnotationPresent(JByte.class)) {
                    getPrimitiveLenght(obj, fields.get(i), data);
                } else {

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
                starIndex += 1;
                num = 1;
                field.setByte(obj, data[starIndex]);
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
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

























