package com.example.myapplication.ui.fragment

import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.MainActivity
import java.util.*

@SuppressLint("NewApi")
class AlarmService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.e("addNotification", "===========create=======")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ServiceCast")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        val date = mSimpleDateFormat.parse(intent.getStringExtra("date"))
        Log.e("addNotification", "onStartCommand:------- ${date}" )
        if (null == timer) {
            timer = Timer()
        }
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                var notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
                var builder =Notification.Builder(applicationContext)
                builder.setSmallIcon(R.drawable.ic_lock_idle_alarm) // 设置通知图标
                builder.setContentText(intent.getStringExtra("contentText")) // 下拉通知啦内容
                builder.setContentTitle(intent.getStringExtra("contentTitle")) // 下拉通知栏标题
                builder.setAutoCancel(true) // 点击弹出的通知后,让通知将自动取消
                builder.setVibrate(longArrayOf(0, 2000, 1000, 4000)) // 震动需要真机测试-延迟0秒震动2秒延迟1秒震动4秒
                builder.setDefaults(Notification.DEFAULT_ALL) // 设置使用系统默认声音
                // builder.addAction("图标", title, intent); //此处可设置点击后 打开某个页面
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    var channel = NotificationChannel("1","mine",NotificationManager.IMPORTANCE_DEFAULT)
                    notificationManager.createNotificationChannel(channel);
                    builder.setChannelId("1");
                }
                notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }, date)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.e("addNotification", "===========destroy=======")
        super.onDestroy()
    }

    companion object {
        var timer: Timer? = null

        // 清除通知
        fun cleanAllNotification(context:Context) {
            val mn = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mn.cancelAll()
            if (timer != null) {
                timer!!.cancel()
                timer = null
            }
        }

        // 添加通知
        fun addNotification(context: Context,
            date: String, contentTitle: String?, contentText: String?
        ) {
            val intent = Intent(
                context,
                AlarmService::class.java
            )
            intent.putExtra("date", date)
            intent.putExtra("contentTitle", contentTitle)
            intent.putExtra("contentText", contentText)
            context.startService(intent)
        }
    }
}