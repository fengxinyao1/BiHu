package com.example.mac.bihu.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.adapter.MyInsideRecyclerAdapter;
import com.example.mac.bihu.mUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class itemTouchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyInsideRecyclerAdapter adapter;
    private LinearLayoutManager manager;

    private List<String> datelist;
    private List<String> authorAvatarlist;
    private List<String> authorNamelist;
    private List<String> contentlist;
    private int[]         exciting;
    private int[]         naive;
    private int[]       answerId;
    private int[]       best;
    private boolean[] is_exciting;
    private boolean[] is_naive;

    public static int qid;

    private mUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch);
        initData();
        initCenjin();
        initThread();
    }
    private void initCenjin(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView1 = getWindow().getDecorView();
            int option1 = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView1.setSystemUiVisibility(option1);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }
    public void initData(){
        datelist = new ArrayList<>();
        authorAvatarlist = new ArrayList<>();
        authorNamelist = new ArrayList<>();
        contentlist = new ArrayList<>();
        exciting = new int[200];
        naive = new int[200];
        is_exciting = new boolean[200];
        is_naive = new boolean[200];
        answerId = new int[200];
        best = new int[200];
        Bundle bundle = getIntent().getExtras();
        qid = bundle.getInt("qid");
        user = (mUser) getApplication();
    }
    public void initThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://bihu.jay86.com/getAnswerList.php";
                StringBuilder request = new StringBuilder();
                request.append("page="+"0"+"&count=100"+"&qid="+qid+"&token="+user.getToken());
                NetUtils.post(url, request.toString(), new NetUtils.Callback() {
                    @Override
                    public void onResponse(String response)  {
                        try {
                            Log.d("fxy", "onResponse: "+response);
                            Log.d("fxy", "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("data");
                            JSONObject jsonObject1 = new JSONObject(data);
                            String answer = jsonObject1.getString("answers");
                            JSONArray jsonArray = new JSONArray(answer);
                            for(int i = 0 ; i < jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                datelist.add(object.getString("date"));
                                authorNamelist.add(object.getString("authorName"));
                                contentlist.add(object.getString("content"));
                                exciting[i] = object.getInt("exciting");
                                best[i] = object.getInt("best");
                                authorAvatarlist.add(object.getString("authorAvatar"));
                                naive[i] = object.getInt("naive");
                                is_exciting[i] = object.getBoolean("is_exciting");
                                is_naive[i] = object.getBoolean("is_naive");
                                answerId[i] = object.getInt("id");
                            }
                            initRecyclerview();
                            initItemButton();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
    public void initRecyclerview(){
        recyclerView = findViewById(R.id.inside_recyclerview);
        adapter = new MyInsideRecyclerAdapter(datelist,authorNamelist,authorAvatarlist,contentlist,exciting,naive,
                is_exciting,is_naive,best,answerId,itemTouchActivity.this);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    public void initItemButton(){
        Button btn_back = findViewById(R.id.inside_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemTouchActivity.this.finish();
            }
        });
    }
}
