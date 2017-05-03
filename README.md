# ByteParser
一个简洁轻便的Byte工具，用于将byte、Object互相转换

### 使用情景 ###
1.做智能家居、或者蓝牙设备，由于通信的资源有限，不会采用网络通信过程中常用的Json数据格式，基本上是采用byte数字串来传递数据。
2.本工具能够将byte数组 转成 Object，或者将Object转成byte数组。

### 使用介绍 ###
在util包下有一下几个class
1.ByteUtil：最基础的byte与基础数据类型、String的互相转换；
2.JByte：注解类 index-标识成员变量的解析顺序， lenght-标识list/array的总长度；
3.JByteUtil: 封装类，封装了JByteToObj, JObjToByte;
4.JByteToObj: 将Byte数组转成Object;
5.JObjToByte: 将Object 转成 byte数组;

使用过程中 只用调用 JByteUtil;

### 使用示例 ###

数据类型
```java 
public class DemoBean{

    @JByte(index = 0)
    public byte aByte;

    @JByte(index = 1)
    public char aChar;

    @JByte(index = 2)
    public short aShort;

    @JByte(index = 3)
    public int aInt;

    @JByte(index = 4)
    public long aLong;

}
```

将数据类型转成byte
```java 
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //准备数据
        DemoBean dataBean = new DemoBean();
        dataBean.aByte = 9;
        dataBean.aShort = 8;
        dataBean.aInt = 7;
        dataBean.aLong = 6l;
        dataBean.aFloat = 5.5f;
        dataBean.aDouble = 4d;

        //调用byte工具 获取byte数组
        byte[] data = JByteUtil.getBytes(dataBean);
}
```

将byte[]转成Object
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DemoBean bean = (DemoBean) JByteUtil.getObject(DemoBean.class, data);
}
```

