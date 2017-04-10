package byteutil.jaelynbtyeutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
        byte[] bytes = aString.getBytes();
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DemoBean dataBean = new DemoBean();
                dataBean.aByte = abyte;
                dataBean.aChar = aChar;
                dataBean.aInt = aInt;
                dataBean.aShort = aShort;
                dataBean.aLong = aLong;
                dataBean.aFloat = aFloat;
                dataBean.aDouble = aDouble;
                dataBean.aString = aString;
                dataBean.aBoolean = true;

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
