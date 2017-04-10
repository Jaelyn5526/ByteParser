package byteutil.jaelynbtyeutil;

/**
 * Created by zaric on 17-04-07.
 */

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

    @JByte(index = 5)
    public float aFloat;

    @JByte(index = 6)
    public double aDouble;

    @JByte(index = 7 , lenght = 20)
    public String aString;

    @JByte(index = 8)
    public boolean aBoolean = true;

    @Override
    public String toString() {
        return "DemoBean{" +
                "aBoolean=" + aBoolean +
                ", aByte=" + aByte +
                ", aChar=" + aChar +
                ", aShort=" + aShort +
                ", aInt=" + aInt +
                ", aLong=" + aLong +
                ", aFloat=" + aFloat +
                ", aDouble=" + aDouble +
                ", aString='" + aString + '\'' +
                '}';
    }
}
