package hbs.com.picnic.utils

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import hbs.com.picnic.utils.AnimationUtils.Property.SCALE_DURATION
import hbs.com.picnic.utils.AnimationUtils.Property.SCALE_INTERPOLATOR
import hbs.com.picnic.utils.AnimationUtils.Property.SIZE_0
import hbs.com.picnic.utils.AnimationUtils.Property.SIZE_1
import hbs.com.picnic.view.ContentViewContract

class AnimationUtils {
    object Property {
        const val SIZE_0 = 0f
        const val SIZE_1 = 1f
        const val SCALE_DURATION = 250L
        val SCALE_INTERPOLATOR = AccelerateInterpolator()
    }

    enum class AnimationType {
        FULL_TO_EMPTY, EMPTY_TO_FULL
    }

    companion object {
        fun animatingSmallView(): ScaleAnimation {
            return ScaleAnimation(SIZE_1, SIZE_0, SIZE_1, SIZE_0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
                fillAfter = true
                duration = SCALE_DURATION
                interpolator = SCALE_INTERPOLATOR
            }
        }

        fun animatingLargeView(): ScaleAnimation {
            return ScaleAnimation(SIZE_0, SIZE_1, SIZE_0, SIZE_1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
                fillAfter = true
                duration = SCALE_DURATION
                interpolator = SCALE_INTERPOLATOR
            }
        }

        fun animateScale(view: View, animationListener: Animation.AnimationListener) {
            val smallAnimation = animatingSmallView()
            smallAnimation.setAnimationListener(animationListener)
            view.startAnimation(smallAnimation)
        }

        fun addAnimateListener(presenter: ContentViewContract.Presenter, view: ImageView, animationType: AnimationType): Animation.AnimationListener {
            return object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    animation?.setAnimationListener(null)
                    presenter.changeImageResource(view, animationType)
                    view.startAnimation(animatingLargeView())
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            }
        }
    }
}