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
        final Boolean aBoolean = false;

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
                dataBean.bBoolean = false;
                dataBean.bByte = 9;
                dataBean.bShort = 8;
                dataBean.bInt = 7;
                dataBean.bLong = 6l;
                dataBean.bFloat = 5.5f;
                dataBean.bDouble = 4d;
                /*dataBean.integers.add(1);
                dataBean.integers.add(2);
                dataBean.integers.add(3);
                dataBean.integers.add(4);
                dataBean.integers.add(5);
                dataBean.integers.add(6);*/

//                byte[] data = JByteUtil.objectToByte(dataBean);

                byte[] data = JObjToByte.getBytes(dataBean);
                Log.d("data", ByteUtil.getStringforLog(data));
                DemoBean bean =
                        (DemoBean) JByteToObj.getObject(DemoBean.class, data);
                if (bean == null) {
                    Log.d("tag--", "null");
                } else {
                    Log.d("tag---", bean.toString());
                }
            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DemoListBean dataBean = new DemoListBean();
                dataBean.integers.add(1);
                dataBean.integers.add(2);
                dataBean.integers.add(3);
                dataBean.integers.add(4);
                dataBean.integers.add(5);
                dataBean.integers.add(6);

                dataBean.a[0] = 1;
                dataBean.a[1] = 2;
                dataBean.a[2] = 3;
                dataBean.a[3] = 4;
                dataBean.a[4] = 5;
                dataBean.a[5] = 6;
                byte[] data = JObjToByte.getBytes(dataBean);
                Log.d("data", ByteUtil.getStringforLog(data));

                DemoListBean bean =
                        (DemoListBean) JByteToObj.getObject(DemoListBean.class, data);
                if (bean == null) {
                    Log.d("tag--", "null");
                } else {
                    Log.d("tag---", bean.toString());
                }
            }
        });
    }
}
