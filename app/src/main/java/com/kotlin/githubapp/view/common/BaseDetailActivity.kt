package com.kotlin.githubapp.view.common

import android.app.Activity
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.bennyhuo.tieguanyin.annotations.ActivityBuilder
import com.bennyhuo.tieguanyin.annotations.PendingTransition
import com.kotlin.githubapp.R
import org.jetbrains.anko.dip

@ActivityBuilder(pendingTransition = PendingTransition(enterAnim = R.anim.rignt_in, exitAnim = R.anim.left_out))
abstract class BaseDetailActivity : AppCompatActivity() {

    private val swipeBackTouchDelegate by lazy { SwipeBackTouchDelegate(this, ::finish) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_in, R.anim.rignt_out)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return swipeBackTouchDelegate.onTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }
}

class SwipeBackTouchDelegate(private val activity: Activity, block: () -> Unit) {

    companion object {
        private const val MIN_FLING_TO_BACK = 2000
    }

    private val minFlingToBack by lazy {
        activity.dip(MIN_FLING_TO_BACK)
    }

    private val swipeBackDelegate by lazy {
        GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                return if (velocityX > minFlingToBack) {
                    block()
                    true
                } else {
                    false
                }
            }
        })
    }

    fun onTouchEvent(event: MotionEvent) = swipeBackDelegate.onTouchEvent(event)
}