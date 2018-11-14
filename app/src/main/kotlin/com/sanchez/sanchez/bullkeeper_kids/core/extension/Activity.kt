package com.sanchez.sanchez.bullkeeper_kids.core.extension

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Add Fragment
 */
fun AppCompatActivity.addFragment(containerViewId: Int, fragment: Fragment, addToBackStack: Boolean) {
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.add(containerViewId, fragment)
    if (addToBackStack)
        fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()
}

/**
 * Add Fragment
 */
fun AppCompatActivity.addFragment(containerViewId: Int, fragment: Fragment, addToBackStack: Boolean, tag: String) {
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.add(containerViewId, fragment)
    if (addToBackStack)
        fragmentTransaction.addToBackStack(tag)
    fragmentTransaction.commit()
}