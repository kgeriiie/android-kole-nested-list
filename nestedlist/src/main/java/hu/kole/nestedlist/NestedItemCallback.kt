package hu.kole.nestedlist

import android.support.v7.util.DiffUtil

abstract class NestedItemCallback<T>: DiffUtil.ItemCallback<T>() {
    abstract fun areNestedContentsTheSame(old: T, new: T): Boolean
}