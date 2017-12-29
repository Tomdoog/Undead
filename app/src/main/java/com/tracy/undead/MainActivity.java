package com.tracy.undead;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tracy.undead.service.BackgroundService;
import com.tracy.undead.service.GrayService;
import com.tracy.undead.service.WhiteService;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    /**
     * 黑色唤醒广播的action
     */
    private final static String BLACK_WAKE_ACTION = "com.wake.black";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_white).setOnClickListener(this);
        findViewById(R.id.btn_gray).setOnClickListener(this);
        findViewById(R.id.btn_black).setOnClickListener(this);
        findViewById(R.id.btn_background_service).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_white) { //系统正常的前台Service，白色保活手段
            Intent whiteIntent = new Intent(getApplicationContext(), WhiteService.class);
            startService(whiteIntent);

        } else if (viewId == R.id.btn_gray) {//利用系统漏洞，灰色保活手段（API < 18 和 API >= 18 两种情况）
            Intent grayIntent = new Intent(getApplicationContext(), GrayService.class);
            startService(grayIntent);

        } else if (viewId == R.id.btn_black) { //拉帮结派，黑色保活手段，利用广播唤醒队友
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
//            intent.addFlags(268468224);
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
            PendingIntent restartIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, 268435456);
//            Intent blackIntent = new Intent();
//            blackIntent.setAction(BLACK_WAKE_ACTION);
//            sendBroadcast(blackIntent);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            PendingIntent operation = PendingIntent.getBroadcast(this, 123, blackIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis()+10000L, restartIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5*1000, restartIntent);
        } else if (viewId == R.id.btn_background_service) {//普通的后台进程
            Intent bgIntent = new Intent(getApplicationContext(), BackgroundService.class);
            startService(bgIntent);

        }
    }
}
