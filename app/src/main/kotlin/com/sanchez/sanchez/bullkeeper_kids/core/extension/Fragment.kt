package com.sanchez.sanchez.bullkeeper_kids.core.extension

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider.Factory
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment

/**
 * In Transaction
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
        beginTransaction().func().commit()

inline fun <reified T : ViewModel> Fragment.viewModel(factory: Factory, body: T.() -> Unit): T {
    val vm = ViewModelProviders.of(this, factory)[T::class.java]
    vm.body()
    return vm
}

/**
 * Close
 */
fun BaseFragment.close() = fragmentManager?.popBackStack()

/**
 * App Context
 */
val BaseFragment.appContext: Context get() = activity?.applicationContext!!



