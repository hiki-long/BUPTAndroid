package com.example.myapplication.ui.uti

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime

class UtiFunc {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun Time2String(time: OffsetDateTime): String {
            val Hour = String.format("%02d", time.hour)
            val Minute = String.format("%02d", time.minute)
            return "${time.toLocalDate()} ${Hour}时 ${Minute}分"
        }
    }
}