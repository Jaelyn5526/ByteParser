package byteutil.jaelyn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import byteutil.jaelyn.util.JByte;

/**
 * Created by zaric on 17-04-07.
 */

public class DemoListBean {

   @JByte(index = 1, lenght = 6)
   ArrayList<Integer> integers = new ArrayList<>();

   @JByte(index = 2, lenght = 6)
   int[] a = new int[10];

   public String getArrayString(List<?> list){
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < list.size(); i++) {
         sb.append(i);
         sb.append('=');
         sb.append(list.get(i));
         sb.append(',');
      }
      return sb.toString();
   }

   @Override
   public String toString() {
      return "DemoListBean{" +
              "a=" + Arrays.toString(a) +
              ", integers=" + getArrayString(integers) +
              '}';
   }
}
