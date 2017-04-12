package byteutil.jaelynbtyeutil;

import java.util.ArrayList;

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

    @JByte(index = 9)
    public Byte bByte;


    @JByte(index = 12)
    public Short bShort;

    @JByte(index = 13)
    public Integer bInt;

    @JByte(index = 14)
    public Long bLong;



    @JByte(index = 15)
    public Float bFloat;

    @JByte(index = 16)
    public Double bDouble;

    @JByte(index = 18)
    public Boolean bBoolean;
  /* @JByte(index = 1)
    ArrayList<Integer> integers = new ArrayList<>();*/

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
                ", bByte=" + bByte +
                ", bShort=" + bShort +
                ", bInt=" + bInt +
                ", bLong=" + bLong +
                ", bFloat=" + bFloat +
                ", bDouble=" + bDouble +
                ", bBoolean=" + bBoolean +
                '}';
    }
}
