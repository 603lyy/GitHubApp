package com.kotlin.githubapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import cn.carbs.android.avatarimageview.library.AvatarImageView
import com.kotlin.githubapp.R
import com.kotlin.githubapp.utils.markdownText
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk15.listeners.onClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return AboutFragmentUI().createView(AnkoContext.create(context!!, this))
    }
}

class AboutFragmentUI : AnkoComponent<AboutFragment> {
    override fun createView(ui: AnkoContext<AboutFragment>): View = ui.apply {
        nestedScrollView {
            verticalLayout {

                avatarImageView {
                    setTextAndColorSeed("abc", "1234")
                }.lparams(width = dip(60), height = dip(60)) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                imageView {
                    imageResource = R.mipmap.ic_launcher
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                themedTextView("GitHub", R.style.detail_title) {
                    textColorResource = R.color.colorPrimary
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                themedTextView("By lyy", R.style.detail_description) {
                    textColorResource = R.color.colorPrimary
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                themedTextView(R.string.open_source_licenses, R.style.detail_description) {
                    textColorResource = R.color.colorPrimary

                    onClick {
                        alert {
                            customView {
                                scrollView {
                                    textView {
                                        padding = dip(10)
                                        markdownText =
                                            context.assets.open("licenses.md").bufferedReader()
                                                .readText()
                                    }
                                }
                            }
                        }.show()
                    }
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }.lparams(width = wrapContent, height = wrapContent) {
                gravity = Gravity.CENTER
            }
        }
    }.view
}

inline fun ViewManager.avatarImageView(): AvatarImageView = avatarImageView() {}
inline fun ViewManager.avatarImageView(init: (@AnkoViewDslMarker AvatarImageView).() -> Unit): AvatarImageView {
    return ankoView({ ctx: Context -> AvatarImageView(ctx) }, theme = 0) { init() }
}
