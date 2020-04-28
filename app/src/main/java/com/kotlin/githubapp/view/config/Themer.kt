package com.kotlin.githubapp.view.config

import android.app.Activity
import androidx.annotation.StyleRes
import com.kotlin.githubapp.R
import com.kotlin.githubapp.settings.Settings

object Themer {
    enum class ThemeMode(@StyleRes val normal: Int){
        DAY(R.style.AppTheme), NIGHT(R.style.AppTheme_Dark)
    }

    fun applyProperTheme(activity: Activity){
        activity.setTheme(currentTheme().normal)
    }

    fun currentTheme() = ThemeMode.valueOf(Settings.themeMode)

    fun toggle(activity: Activity){
        when(currentTheme()){
            Themer.ThemeMode.DAY -> Settings.themeMode = ThemeMode.NIGHT.name
            Themer.ThemeMode.NIGHT -> Settings.themeMode = ThemeMode.DAY.name
        }
        activity.recreate()
    }
}