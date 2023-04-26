package com.lncdriver.dbh.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lncdriver.dbh.utils.FragmentCallback

/**
 * Create by Siru Malayil on 26-04-2023.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    fun showAlertMessageDialog(
        message: String?,
        negativeButton: Boolean = false,
        showRetry: Boolean = false,
        fragmentCallback: FragmentCallback? = null
    ) {
        val msgFragment = AlertMessageDialogFragment.newInstance(
            message = message,
            showRetry = showRetry,
            negativeButton = negativeButton,
            fragmentCallBack = fragmentCallback
        )
        msgFragment.show(supportFragmentManager, "msg")
    }
}