package com.globe.platform.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.reflect.KClass

inline fun <reified T : Fragment> Fragment.replaceIfNoPrevious(
    @IdRes placeHolder: Int,
    block: () -> T
) {
    childFragmentManager.replaceIfNoPrevious(placeHolder, block)
}

inline fun <reified T : Fragment> Fragment.replaceIfNoPrevious(items: List<Pair<Int, (() -> T)>>) {
    childFragmentManager.replaceIfNoPrevious(items)
}

inline fun <reified T : Fragment> FragmentManager.replaceIfNoPrevious(
    @IdRes placeHolder: Int,
    block: () -> T
) {
    val tag = getTag(T::class)
    val retrievedFragment = findByTagOrNull<T>(tag)
    if (retrievedFragment == null) {
        beginTransaction()
            .replace(placeHolder, block(), tag)
            .commitNowAllowingStateLoss()
    }
}

inline fun <reified T : Fragment> FragmentManager.replaceIfNoPrevious(items: List<Pair<Int, (() -> T)>>) {
    val fragments: List<Triple<Int, String, () -> T>> = items
        .map { Triple(it.first, getTag(T::class), it.second) }
        .filter { findByTagOrNull<T>(it.second) == null }
    if (fragments.isEmpty()) return
    val transaction = beginTransaction()
    fragments.forEach { (containerId, tag, block) ->
        transaction.replace(containerId, block(), tag)
    }
    transaction.commitNowAllowingStateLoss()
}

fun getTag(type: KClass<*>): String = type.java.name

inline fun <reified T : Fragment> FragmentManager.findByTagOrNull(tag: String): T? {
    return this.findFragmentByTag(tag) as? T
}

inline fun <reified T : Fragment> Fragment.add(@IdRes placeHolder: Int, fragment: T) =
    childFragmentManager.add(placeHolder, fragment)

inline fun <reified T : Fragment> Fragment.add(@IdRes placeHolder: Int, fragment: T, tag: String) =
    childFragmentManager.add(placeHolder, fragment, tag)

fun Fragment.remove() {
    if (isAdded) {
        parentFragment?.childFragmentManager?.remove(this)
    }
}

inline fun <reified T : Fragment> FragmentManager.findByTagOrNull(): T? {
    val tag = getTag(T::class)
    return this.findFragmentByTag(tag) as? T
}

inline fun <reified T : Fragment> FragmentManager.add(@IdRes placeHolder: Int, fragment: T) {
    val retrievedFragment = findByTagOrNull<T>()
    if (retrievedFragment == null) {
        val tag = getTag(fragment::class)
        beginTransaction()
            .add(placeHolder, fragment, tag)
            .commitNow()
    }
}

inline fun <reified T : Fragment> FragmentManager.add(
    @IdRes placeHolder: Int,
    fragment: T,
    tag: String
) {
    val retrievedFragment = findByTagOrNull<T>(tag)
    if (retrievedFragment == null) {
        beginTransaction()
            .add(placeHolder, fragment, tag)
            .commitNow()
    }
}

fun FragmentManager.remove(fragment: Fragment) {
    beginTransaction()
        .remove(fragment)
        .commitAllowingStateLoss()
}
