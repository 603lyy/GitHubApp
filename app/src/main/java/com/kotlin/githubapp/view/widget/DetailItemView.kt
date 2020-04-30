package com.kotlin.githubapp.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton
import com.kotlin.githubapp.R
import com.kotlin.githubapp.utils.subscribeIgnoreError
import kotlinx.android.synthetic.main.detail_item.view.*
import org.jetbrains.anko.sdk15.listeners.onClick
import rx.Observable
import kotlin.reflect.KProperty

/**
 * 做转换，把boolean值变成rxjava的一个Observable对象
 */
typealias CheckEvent = (Boolean) -> Observable<Boolean>

/**
 * 因为下方好多get和set，是对控件的某个属性进行代理操作
 * 这里创建一个属性代理的类
 * T：这个属性它的值是什么类型
 * R：它的receiver是什么类型
 */
class ObjectPropertyDelegate<T, R>(
    val receiver: R,
    val getter: ((R) -> T)? = null,
    val setter: ((R, T) -> Unit)? = null,
    defaultValue: T? = null
) {
    private var value: T? = defaultValue

    operator fun getValue(ref: Any, property: KProperty<*>): T {
        return getter?.invoke(receiver) ?: value!!
    }

    operator fun setValue(ref: Any, property: KProperty<*>, value: T) {
        setter?.invoke(receiver, value)
        this.value = value
    }
}

class DetailItemView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    /**
     * 为了空指针问题，下方的title等熟悉要创建了才能使用
     */
    init {
        View.inflate(context, R.layout.detail_item, this)
    }

    var title by ObjectPropertyDelegate(titleView, TextView::getText, TextView::setText, "")

    var content by ObjectPropertyDelegate(contentView, TextView::getText, TextView::setText, "")

    var icon by ObjectPropertyDelegate(iconView, null, ImageView::setImageResource, 0)

    var operatorIcon by ObjectPropertyDelegate(
        operatorIconView,
        null,
        ToggleButton::setBackgroundResource,
        0
    )

    var isChecked by ObjectPropertyDelegate(
        operatorIconView,
        ToggleButton::isChecked,
        ToggleButton::setChecked
    )

    var checkEvent: CheckEvent? = null

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.DetailItemView)
            title = a.getString(R.styleable.DetailItemView_item_title) ?: ""
            content = a.getString(R.styleable.DetailItemView_item_content) ?: ""
            icon = a.getResourceId(R.styleable.DetailItemView_item_icon, 0)
            operatorIcon = a.getResourceId(R.styleable.DetailItemView_item_op_icon, 0)
            a.recycle()

            onClick {
                checkEvent?.invoke(isChecked)
                    ?.subscribeIgnoreError {
                        isChecked = it
                    }
            }
        }
    }
}
