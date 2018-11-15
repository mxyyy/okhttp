package com.bwie.post01;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edUsername;
    private EditText edPassword;
    private Button btnPost;
    private Button btnGet;
    private TextView tvStatus;
    public static final int SUCCESS = 567;
    public static final int FALSE = 123;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

           switch (msg.what){
               case SUCCESS:
                   String text = (String) msg.obj;
                   tvStatus.setText(text);
                   break;
               case FALSE:
                   Toast.makeText(MainActivity.this, "无网络", Toast.LENGTH_SHORT).show();
                   break;
           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        btnPost = findViewById(R.id.btn_post);
        btnGet = findViewById(R.id.btn_get);
        tvStatus = findViewById(R.id.tv_status);

        btnPost.setOnClickListener(this);
        btnGet.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                String username = edUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //1.创建okHttpClient
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .build();
                FormBody formBody = new FormBody.Builder()
                        .add("qq", username)
                        .add("pwd", password)
                        .build();
                //创建request
                Request request = new Request.Builder()
                        .post(formBody)
                        .url("http://169.254.53.96:8080/web/LoginServlet")
                        .build();
                //创建Call对象
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(FALSE);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Message msg = Message.obtain();
                        msg.obj = string;
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);
                    }
                });

                break;
            case R.id.btn_get:

                break;
        }

    }
}
