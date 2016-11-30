package com.example.acer.mytext;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        mWeb.addJavascriptInterface(new AndroidBridge(handler), "android");
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

        public AndroidBridge(Handler handler) {
            this.handler = handler;
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
}
