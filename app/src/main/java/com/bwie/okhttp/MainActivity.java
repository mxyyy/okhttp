package com.bwie.okhttp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String Path = "http://publicobject.com/helloworld.txt";

    private TextView mText_tv;
    public static final int SUCCESS = 993;
    public static final int FALL =814;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //加载网络成功，进行UI的更新
                case SUCCESS:
                    String text = (String)msg.obj;
                    mText_tv.setText(text);
                      break;
                    //当加载网络失败，执行逻辑代码
                case FALL:
                    Toast.makeText(MainActivity.this, "天气不好 网络异常", Toast.LENGTH_SHORT).show();
                      break;
                  default:
                      break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
    }

    private void initView() {
      mText_tv = findViewById(R.id.text_tv);
    }
    public void okhttp_ok(View view){
        new Thread() {
            @Override
            public void run() {

              OkHttpClient client = new OkHttpClient.Builder().build();
              Request request  =new Request.Builder().url(Path).build();
              Response response = null;


                  try {
                      response = client.newCall(request).execute();
                      //重定向
                      System.out.print("Response 1 response:                 "+response);
                      System.out.print("Response 1 response:                 "+response.cacheResponse());
                      System.out.print("Response 1 response:                 "+response.networkResponse());
                      String  string = response.body().string();
                      //通过handler对象，把数据传到主线程，进行UI更新
                      Message obtain = Message.obtain();
                      obtain.obj = string;
                      obtain.what = SUCCESS;
                      handler.sendMessage(obtain);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }


              }
            }.start();
        }
    }

