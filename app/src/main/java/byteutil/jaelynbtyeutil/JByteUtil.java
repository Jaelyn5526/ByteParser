package byteutil.jaelynbtyeutil;

import android.util.Log;
import android.widget.TextView;

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
        List<Field> fields = extractFields(cls);
        return parserByte(cls, data, fields);
    }

    /**
     * 提取需要解析的成员变量
     */
    private static List<Field> extractFields(Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> arrayFields = new ArrayList<>();
        if (List.class.isAssignableFrom(cls) | cls.isArray()){
            arrayFields = Arrays.asList(fields);
        }else {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isAnnotationPresent(JByte.class)) {
                    arrayFields.add(fields[i]);
                }
                Log.d("field", fields[i].getName() + "---" + fields[i].getType());
            }
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

    private static Object parserByte(Class<?> cls, byte[] data, List<Field> fields) {

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
                field.setByte(obj, data[starIndex]);
                starIndex += 1;
            } else if (field.getType() == char.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 2);
                starIndex += 2;
                field.setChar(obj, ByteUtil.getChar(bytes));
            } else if (field.getType() == short.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 2);
                starIndex += 2;
                field.setShort(obj, ByteUtil.getShort(bytes));
            } else if (field.getType() == int.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 4);
                starIndex += 4;
                field.setInt(obj, ByteUtil.getInt(bytes));
            } else if (field.getType() == long.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 8);
                starIndex += 8;
                field.setLong(obj, ByteUtil.getLong(bytes));
            } else if (field.getType() == float.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 4);
                starIndex += 4;
                field.setFloat(obj, ByteUtil.getFloat(bytes));
            } else if (field.getType() == double.class) {
                byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 8);
                starIndex += 8;
                field.setDouble(obj, ByteUtil.getDouble(bytes));
            }  else if (field.getType() == boolean.class){
                boolean bln = data[starIndex] == 1;
                field.setBoolean(obj, bln);
                starIndex += 1;
            }
        } catch (IllegalAccessException e) {
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
                    if (data[i] == 0) {
                        stringSize = i - starIndex;
                        break;
                    }
                }
                byte[] bytes = new byte[stringSize];
                System.arraycopy(data, starIndex, bytes, 0, stringSize);
                String string = new String(bytes, "UTF-8");
                field.set(obj, string);
                starIndex += lenght;
            }else if (field.getType() == Boolean.class){
                boolean bln = data[starIndex] == 1;
                field.set(obj, bln);
                starIndex += 1;
            }else if (Number.class.isAssignableFrom(field.getType())){
                getNumberLenght(obj, field, data);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static void getNumberLenght(Object obj, Field field, byte[] data)
            throws IllegalAccessException {
        if (field.getType() == Byte.class) {
            field.set(obj, data[starIndex]);
            starIndex += 1;
        } else if (field.getType() == Short.class) {
            byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 2);
            field.set(obj, ByteUtil.getShort(bytes));
            starIndex += 2;
        } else if (field.getType() == Integer.class) {
            byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 4);
            field.set(obj, ByteUtil.getInt(bytes));
            starIndex += 4;
        } else if (field.getType() == Long.class) {
            byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 8);
            field.set(obj, ByteUtil.getLong(bytes));
            starIndex += 8;
        } else if (field.getType() == Float.class) {
            byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 4);
            field.set(obj, ByteUtil.getFloat(bytes));
            starIndex += 4;
        } else if (field.getType() == Double.class) {
            byte[] bytes = Arrays.copyOfRange(data, starIndex, starIndex + 8);
            field.set(obj, ByteUtil.getDouble(bytes));
            starIndex += 8;
        }
    }

    public static byte[] objectToByte(Object object) {
        Class cls = object.getClass();
        List<Field> fields = extractFields(cls);
        return objectToByte(object, fields);
    }

    private static byte[] objectToByte(Object obj, List<Field> fields) {
        byte[] bytes = new byte[0];
        byte[] data = new byte[0];
        byte[] current;
        for (Field field : fields) {
            if (field.getType().isPrimitive()) {
                current = getPrimitiveByte(field, obj);
            } else {
                current = getUnPrimitiveByte(field, obj);
            }
            if (current != null){
                bytes = Arrays.copyOf(data, data.length + current.length);
                System.arraycopy(current, 0, bytes, data.length, current.length);
                data = bytes;
            }
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
            if (field.getType() == String.class) {  //String 类型
                byte[] data = new byte[field.getAnnotation(JByte.class).lenght()];
                byte[] strings = ((String) field.get(obj)).getBytes();
                System.arraycopy(strings, 0, data, 0, strings.length);
                return data;
            } else if (Number.class.isAssignableFrom(field.getType())) {
                return getNumberByte(field, obj);
            } else if (field.getType() == Boolean.class) {
                byte data = ((Boolean) field.get(obj)) ? (byte) 1 : (byte) 0;
                return new byte[]{data};
            } else if(List.class.isAssignableFrom(field.getType())){
                List<Object> lists = (List<Object>) field.get(obj);
                byte[] bytes = new byte[0];
                byte[] datas = new byte[0];
                byte[] currents = new byte[0];
                for (int i = 0; i < lists.size(); i++) {
                    Field[] fields = lists.get(i).getClass().getFields();
                    List<Field> arrayField = Arrays.asList(fields);
                    currents = objectToByte(lists.get(i), arrayField);
                    bytes = Arrays.copyOf(datas, datas.length + currents.length);
                    System.arraycopy(currents, 0, bytes, datas.length, currents.length);
                }
                return bytes;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getNumberByte(Field field, Object obj) throws IllegalAccessException {
            if (field.getType() == Byte.class) {
                Byte data = (Byte) field.get(obj);
                return new byte[]{data};
            } else if (field.getType() == Short.class) {
                Short data = (Short) field.get(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == Integer.class) {
                Integer data = (Integer) field.get(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == Long.class) {
                Long data = (Long) field.get(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == Float.class) {
                Float data = (Float) field.get(obj);
                return ByteUtil.getBytes(data);
            } else if (field.getType() == Double.class) {
                Double data = (Double) field.get(obj);
                return ByteUtil.getBytes(data);
            }

        return null;
    }
}