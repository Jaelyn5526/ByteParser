package byteutil.jaelynbtyeutil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zaric on 17-04-07.
 */

public class DemoListBean {

   @JByte(index = 1, lenght = 6)
   ArrayList<Integer> integers = new ArrayList<>();

//   @JByte(index = 2)
   int[] a = new int[10];

   @Override
   public String toString() {
      return "DemoListBean{" +
              "a=" + Arrays.toString(a) +
              ", integers=" + integers +
              '}';
   }
}
