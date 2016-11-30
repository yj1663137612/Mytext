package com.example.acer.mytext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_main)
    Button mBtn;
    @BindView(R.id.btn_main_one)
    Button mBtn_one;
    @BindView(R.id.txt_main)
    TextView mTxt;
    @BindView(R.id.web_main)
    WebView mWeb;
    @BindColor(R.color.red)
    int mClor;
    @BindColor(R.color.colorAccent)
    int mClor_one;
    Handler handler;
    @BindView(R.id.lst_main)
    ListView mLst_main;
    String path;
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //加载组件
        handler = new Handler();

        setWeb();
        handler.sendEmptyMessage(1);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("天王盖地虎" + i);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        mLst_main.setAdapter(adapter);
    }

    @OnItemClick(R.id.lst_main)
    public void setList(int position) {
        Toast.makeText(this, "宝塔镇河妖" + position, Toast.LENGTH_SHORT).show();
    }

    //设置WEB
    private void setWeb() {

        //第一步：设置WebView允许JavaScript运行
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);

        //第二步：为WebView添加一个JavascriptInterface的接口对象，这个方法接收两个参数
        //      第一个参数就是自定义的接口对象，第二个参数是一个字符串，用于js中调用android的方法
        mWeb.addJavascriptInterface(new AndroidBridge(handler, this), "android");
        //加载网页
        mWeb.loadUrl("file:///android_asset/idex.html");

    }

    @OnClick({R.id.btn_main, R.id.btn_main_one})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main:
                mTxt.setTextColor(mClor);
                mWeb.loadUrl("javascript:useJs()");
                Toast.makeText(this, "你好", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_main_one:
                mTxt.setTextColor(mClor_one);
                Toast.makeText(this, "你也好", Toast.LENGTH_SHORT).show();
                break;
        }
    }

//第五步 :安卓调用网页上的JS代码，使用的是loadUrl方法，传入一个字符串格式为:js:方法名


//    @Override
//    public void onClick(View v) {
//        mWeb.loadUrl("javascript:useJs()");
//    }

    //js调用安卓代码
    //步骤
    //1.在接口类声明一个方法（安卓的），加JavascriptInterface这样声明一个类
//    2.在js里使用    （指定的字符串），这里咋们使用（android）.接口类中的方法名
    //第三步：声明一个类，这个类就是接口类
    class AndroidBridge {

        Handler handler;
        Context context;

        public AndroidBridge(Handler handler, Context context) {
            this.context = context;
            this.handler = handler;
        }

        @JavascriptInterface
        public void getCamera() {
            /*分析：1：系统相机是系统的东西，调用的时候需要权限:android.permission.CAMERA
                * */
            //2：使用意图来调用相机:MediaStore媒体中心，ACTION_IMAGE_CAPTURE：照相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //3：照相的时候，照的照片需要保存下来，所以需要提供一个路径，用于保存照片
            //判断内存卡是否挂载
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //获取地址
                path = Environment.getExternalStorageDirectory().getPath();
            }
            //获取文件
            file = new File(path + File.separator + System.currentTimeMillis()+".jpg");
            Log.e("地址", "" + file);
            //设置保存路径  MediaStore.EXTRA_OUTPUT,:指的是媒体向出输出
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            //启动回调  requestCode:请求码，>=0
            startActivityForResult(intent, 1);
        }

        @JavascriptInterface
        public void changeTxt() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mTxt.setText("收到了一条来自JS的信息");
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                    Intent intent = new Intent(this, CameraActivity.class);
                    intent.putExtra("img", bitmap);
                    Log.e("===", "000" + bitmap);
                    startActivity(intent);
                    break;
            }
        }
    }
}
