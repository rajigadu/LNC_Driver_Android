package com.lncdriver.dbh.extension

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.lncdriver.R

/**
 * Create by Siru Malayil on 26-04-2023.
 */

fun AppCompatActivity.navigate(
    fragment: Fragment,
    addToBackStack: Boolean = true,
    animation: Boolean = true,
    backStackName: String = "",
) {

    val rootView: ViewGroup = findViewById(android.R.id.content)
    val container = rootView.findViewById<FrameLayout>(R.id.frame)
        ?: throw Throwable("Activity FrameLayout id needs to be \"container\"")
    if (animation) {
        add(fragment, container.id, addToBackStack, backStackName)
    } else {
        addWithoutAnim(
            fragment,
            container.id,
            addToBackStack,
            backStackName
        )
    }
}

fun replaceFragment(fragment: Fragment, fragmentManager: FragmentManager) {
    val transaction = fragmentManager.beginTransaction()
    transaction.replace(R.id.frame, fragment)
    transaction.commit()
}

fun AppCompatActivity.add(
    fragment: Fragment,
    @IdRes container: Int,
    addToBackStack: Boolean = false,
    backStackName: String = "",
) {
    supportFragmentManager.transact {
        add(container, fragment, backStackName)
        if (addToBackStack) addToBackStack(backStackName)
    }
}
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}

fun AppCompatActivity.addWithoutAnim(
    fragment: Fragment,
    @IdRes container: Int,
    addToBackStack: Boolean = false,
    backStackName: String = ""
) {
    supportFragmentManager.transact {
        add(container, fragment, backStackName)
        if (addToBackStack) addToBackStack(backStackName)
    }
}