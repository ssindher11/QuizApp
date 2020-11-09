package com.ssindher.quizapp

import android.app.Application
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Instabug.Builder(this, getString(R.string.instabug_key))
            .setInvocationEvents(
                InstabugInvocationEvent.SHAKE,
                InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT
            )
            .build()
    }
}