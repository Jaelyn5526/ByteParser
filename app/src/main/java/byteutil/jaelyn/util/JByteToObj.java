package byteutil.jaelyn.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by zaric on 17-04-12.
 */

public class JByteToObj {
    private static int index = 0;

    public static Object getObject(Class<?> cls, byte... bytes) {
        if (bytes == null | bytes.length == 0) {
            return null;
        }
        index = 0;
        return parserObject(cls, bytes.length, bytes);
    }

    /**
     * 分类解析数据
     * 数据类型一：基础数据类型，Number，String等可以直接转成byte[]
     * 数据类型二：Class list array
     * @param cls
     * @param length
     * @param bytes
     * @return
     */
    private static Object parserObject(Class<?> cls, int length, byte... bytes) {
        if (isBaseVariable(cls)) {
            return parserVariable(cls, length, bytes);
        } else {
            return parserChildObject(cls, length, bytes);
        }
    }

    /**
     * 将 数据类型一 数据转成byte[]
     * @param cls
     * @param lenght array list String 的长度
     * @param bytes
     * @return
     */
    private static Object parserVariable(Class<?> cls, int lenght, byte... bytes) {

        if (cls == byte.class | cls == Byte.class) {
            if (bytes.length <= index) {
                return null;
            }
            byte data = bytes[index];
            index += 1;
            return data;
        } else if (cls == char.class | cls == Character.class) {
            if (bytes.length <= index + 1) {
                return null;
            }
            char data = ByteUtil.getChar(bytes[index], bytes[index + 1]);
            index += 2;
            return data;
        } else if (cls == short.class | cls == Short.class) {
            if (bytes.length <= index + 1) {
                return null;
            }
            short data = ByteUtil.getShort(bytes[index], bytes[index + 1]);
            index += 2;
            return data;
        } else if (cls == int.class | cls == Integer.class) {
            if (bytes.length <= index + 3) {
                return null;
            }
            int data = ByteUtil.getInt(bytes[index], bytes[index + 1], bytes[index + 2],
                    bytes[index + 3]);
            index += 4;
            return data;
        } else if (cls == long.class | cls == Long.class) {
            if (bytes.length <= index + 7) {
                return null;
            }
            long data = ByteUtil.getLong(bytes[index], bytes[index + 1], bytes[index + 2],
                    bytes[index + 3]
                    , bytes[index + 4], bytes[index + 5], bytes[index + 6], bytes[index + 7]);
            index += 8;
            return data;
        } else if (cls == float.class | cls == Float.class) {
            if (bytes.length <= index + 3) {
                return null;
            }
            float data = ByteUtil.getFloat(bytes[index], bytes[index + 1], bytes[index + 2],
                    bytes[index + 3]);
            index += 4;
            return data;
        } else if (cls == double.class | cls == Double.class) {
            if (bytes.length <= index + 7) {
                return null;
            }
            double data = ByteUtil.getDouble(bytes[index], bytes[index + 1], bytes[index + 2],
                    bytes[index + 3]
                    , bytes[index + 4], bytes[index + 5], bytes[index + 6], bytes[index + 7]);
            index += 8;
            return data;
        } else if (cls == boolean.class || cls == Boolean.class) {
            if (bytes.length <= index) {
                return null;
            }
            boolean data = bytes[index] == (byte) 1;
            index += 1;
            return data;
        } else if (cls == String.class) {
            if (bytes.length <= index + lenght - 1) {
                return null;
            }
            int stringSize = 0;
            for (int i = index; i < index + lenght; i++) {
                if (bytes[i] == 0) {
                    stringSize = i - index;
                    break;
                }
            }
            byte[] datas = new byte[stringSize];
            System.arraycopy(bytes, index, datas, 0, stringSize);
            index += lenght;
            return ByteUtil.getString(datas);
        }
        return null;
    }

    /**
     * 将数据类型二数据 提取解析成 数据类型二，再调用parserVariable，解析获取到byte[]
     * @param cls
     * @param lenght
     * @param bytes
     * @return
     */
    private static Object parserChildObject(Class<?> cls, int lenght, byte... bytes) {
        try {
            Object obj = Class.forName(cls.getName()).newInstance();
            if (List.class.isAssignableFrom(cls)) {
                return null;
            } else if (cls.isArray()) {
                return null;
            } else {
                List<Field> fields = extractFields(cls);
                for (Field field : fields) {
                    Class<?> childCls = field.getType();
                    if (List.class.isAssignableFrom(childCls)) {
                        Type type = field.getGenericType();
                        ParameterizedType pt = (ParameterizedType) type;
                        Type[] arrayChildType = pt.getActualTypeArguments();
                        List<Object> datas = new ArrayList<>();
                        int datasSize = field.getAnnotation(JByte.class).lenght();
                        for (int i = 0; i < datasSize; i++) {
                            Object childObj =
                                    parserObject((Class) arrayChildType[0],
                                            field.getAnnotation(JByte.class).lenght(), bytes);
                            datas.add(childObj);
                        }
                        field.set(obj, datas);
                    } else if (childCls.isArray()) {
                        int datasSize = field.getAnnotation(JByte.class).lenght();
                        Object datas =
                                Array.newInstance(childCls.getComponentType(), datasSize);
                        for (int i = 0; i < datasSize; i++) {
                            Object childObj =
                                    parserObject(childCls.getComponentType(),
                                            field.getAnnotation(JByte.class).lenght(), bytes);
                            Array.set(datas, i, childObj);
                        }
                        field.set(obj, datas);
                    } else {
                        Object childObj =
                                parserObject(childCls, field.getAnnotation(JByte.class).lenght(),
                                        bytes);
                        field.set(obj, childObj);
                    }
                }
                return obj;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提取需要解析的有注解成员变量
     * 按照注解排序
     * @param cls
     * @return
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

    /**
     * 判断数据类型：数据类型一， 数据类型二
     * @param cls
     * @return
     */
    public static boolean isBaseVariable(Class<?> cls) {
        if (cls.isPrimitive() | Number.class.isAssignableFrom(cls) | cls == Character.class |
                cls == String.class | cls == Boolean.class) {
            return true;
        }
        return false;
    }
}
