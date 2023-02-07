package com.example.tachiyomi_clone.ui.base

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.ui.reader.ReaderFragment
import com.example.tachiyomi_clone.utils.inTransaction
import com.example.tachiyomi_clone.utils.system.ScreenUtils
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    HasAndroidInjector {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var binding: B

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        ScreenUtils.applyWindow(window)
        super.onCreate(savedInstanceState)
        setBindingContentView(getLayoutResId())
        initViews(savedInstanceState)
        setEventListener()
    }

    abstract val modelClass: Class<VM>

    protected abstract fun getLayoutResId(): Int

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(modelClass)
    }

    protected open fun setBindingContentView(@LayoutRes layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    protected open fun initViews(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.color_1C1C1E)
    }

    protected open fun setEventListener() {}

    protected fun checkHasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun addFragment(fragment: Fragment, frameId: Int, isAnimated: Boolean = true) {
        val fragmentInBackStack = supportFragmentManager.fragments.filter {
            it.javaClass.name == fragment.javaClass.name
        }
        if (fragmentInBackStack.isEmpty()) {
            supportFragmentManager.inTransaction {

                if (isAnimated) {
                    val slide = Slide(Gravity.END)
                    slide.duration = 300
                    fragment.enterTransition = slide
                }

                if (!fragment.isAdded) {
                    add(frameId, fragment, fragment.javaClass.name)
                    addToBackStack(fragment.javaClass.name)
                }
            }
        }
    }

    fun replaceFragment(
        fragment: Fragment,
        frameId: Int,
        isAddToBackStack: Boolean = true,
        isAnimated: Boolean = true
    ) {
        supportFragmentManager.inTransaction {
            if (isAnimated) {
                val slide = Slide(Gravity.END)
                slide.duration = 300
                fragment.enterTransition = slide
            }

            replace(frameId, fragment, fragment.javaClass.name)
            if (!fragment.isAdded && isAddToBackStack) {
                addToBackStack(fragment.javaClass.name)
            }
        }
    }

    fun popFragment() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finishAfterTransition()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        supportFragmentManager.fragments.forEach {
            if (it is ReaderFragment) return it.onDispatchKeyEvent(
                event,
                super.dispatchKeyEvent(event)
            )
        }
        return super.dispatchKeyEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}