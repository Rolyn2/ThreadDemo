package com.zhouxin.administrator.thearddemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv;
    //从用户角度来看，在Acitivity超过5秒就是耗时操作，在广播接收者是10秒就是耗时操作
    Handler handler=new Handler(){//创建Handler，重写方法
        @Override
        public void handleMessage(Message msg) {//处理消息的方法，消息中转载了子线程的数据
           switch (msg.what){
               case 0:
                   iv.setImageResource(R.drawable.pager_guide1);
                   break;
               case 1:
                   iv.setImageResource(R.drawable.pager_guide2);
                   break;
               case 2:
                   iv.setImageResource(R.drawable.pager_guide3);
                   break;
           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv= (ImageView) findViewById(R.id.iv);

    }

    @Override
    public void onClick(View view) {
        //UI线程就是主线程，不适合执行耗时操作，如果有耗时操作，可能会出现ANR（Applicition No Responed）
        //模拟执行下载操作
        //让线程休眠15秒
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    //方法1：
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            iv.setImageResource(R.drawable.ic_launcher1);
//                        }
//                    });
                    //方法2：
//                    iv.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            iv.setImageResource(R.drawable.ic_launcher1);
//                        }
//                    });
                    //方法3：Handler机制，用来处理子线程的数据，把子线程的数据放在主线程中
                    //创建Message
                    Message message=handler.obtainMessage();
                    message.what=0;
                    message.arg1=1;//放了数据1
                    handler.sendMessage(message);
                    //发送消息，将消息给主线程

                    Thread.sleep(1500);
                    Message message1=handler.obtainMessage();
                    message1.what=1;
                    handler.sendMessage(message1);

                    Thread.sleep(1500);
                    Message message2=handler.obtainMessage();
                    message2.what=2;
                    handler.sendMessage(message2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
}
