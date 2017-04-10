package byteutil.jaelynbtyeutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final byte abyte = 10;
        final char aChar = 'a';
        final short aShort = 12;
        final int aInt = 200;
        final long aLong = 900;
        final float aFloat = 1.9f;
        final double aDouble = 9.4;
        final String aString = "Hello Android";
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DemoBean dataBean = new DemoBean();
                /*dataBean.aByte = abyte;
                dataBean.aChar = aChar;
                dataBean.aInt = aInt;
                dataBean.aShort = aShort;
                dataBean.aLong = aLong;
                dataBean.aFloat = aFloat;
                dataBean.aDouble = aDouble;
                dataBean.aString = aString;
                dataBean.aBoolean = true;
                dataBean.bBoolean = false;
                dataBean.bByte = 9;
                dataBean.bShort = 8;
                dataBean.bInt = 7;
                dataBean.bLong = 6l;
                dataBean.bFloat = 5.5f;
                dataBean.bDouble = 4d;*/
                dataBean.integers.add(1);
                dataBean.integers.add(2);
                dataBean.integers.add(3);
                dataBean.integers.add(4);
                dataBean.integers.add(5);
                dataBean.integers.add(6);

                byte[] data = JByteUtil.objectToByte(dataBean);

                DemoBean bean =
                        (DemoBean) JByteUtil
                                .parserByte(DemoBean.class, data);
                if (bean == null){
                    Log.d("tag--", "null");
                }else {
                    Log.d("tag---", bean.toString());
                }
            }
        });

    }
}
