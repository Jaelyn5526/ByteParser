package byteutil.jaelynbtyeutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        byte abyte = 10;
        char aChar = 'a';
        short aShort = 12;
        int aInt = 200;
        long aLong = 900;
        float aFloat = 1.9f;
        double aDouble = 9.4;

        DemoBean bean =
                (DemoBean) JByteParser.parserByte(DemoBean.class, new byte[]{1, 1, 2, 3, 13, 4, 5,4,4, 1,2,3,4,5,6,7,8, 1,2,3,4, 1,2,3,4,5,6,7,8});
        if (bean != null){
            Log.d("tag--", "null");
        }else {
            Log.d("tag---", bean.toString());
        }
    }
}
