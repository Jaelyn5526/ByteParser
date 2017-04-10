package byteutil.jaelynbtyeutil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zaric on 17-04-07.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface JByte {
    //成员变量的顺序 递增
    int index() default 0;
    int lenght() default 0;
}
