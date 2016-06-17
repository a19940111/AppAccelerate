package com.example.heqian.addspace;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startMemory = getAvailMemory(MainActivity.this);
                System.out.println(startMemory + "");
                clear(MainActivity.this);
                long endMemory = getAvailMemory(MainActivity.this);
                Toast.makeText(MainActivity.this, "清理了"+(endMemory-startMemory)+"MB内存", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getAvailMemory(Context context)
    {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem/(1024*1024);
    }
    private void clear(Context context){
        ActivityManager activityManger=(ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list=activityManger.getRunningAppProcesses();
        if(list!=null)
            for(int i=0;i<list.size();i++)
            {
                ActivityManager.RunningAppProcessInfo apinfo=list.get(i);

                String[] pkgList=apinfo.pkgList;

                if(apinfo.importance>ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE)
                {
                    // Process.killProcess(apinfo.pid);
                    for(int j=0;j<pkgList.length;j++)
                    {
                        //2.2以上是过时的,请用killBackgroundProcesses代替
                        /**清理不可用的内容空间**/
                        //activityManger.restartPackage(pkgList[j]);
                        activityManger.killBackgroundProcesses(pkgList[j]);
                    }
                }
            }
    }
}
