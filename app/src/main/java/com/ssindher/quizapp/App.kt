package com.ssindher.quizapp

import android.app.Application
import com.bugfender.sdk.Bugfender

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Bugfender.init(this, resources.getString(R.string.bugfender_key), BuildConfig.DEBUG)
        Bugfender.enableCrashReporting()
        Bugfender.enableUIEventLogging(this)
        Bugfender.enableLogcatLogging()
    }

}