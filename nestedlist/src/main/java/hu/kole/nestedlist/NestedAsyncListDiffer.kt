package hu.kole.nestedlist

import android.annotation.SuppressLint
import android.support.v7.recyclerview.extensions.AsyncDifferConfig.Builder
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import android.util.Log
import java.util.*

open class NestedAsyncListDiffer<T>(val adapter: ListAdapter<T, *>, diffCallback: DiffUtil.ItemCallback<T>) {

    private val mUpdateCallback: ListUpdateCallback
    private val mConfig: AsyncDifferConfig<T>

    private var mList: List<T>? = null
    private var mReadOnlyList: List<T> = listOf()
    private var mMaxScheduledGeneration: Int = 0

    init {
        mUpdateCallback = AdapterListUpdateCallback(adapter)
        mConfig = Builder(diffCallback).build()
    }

    @SuppressLint("RestrictedApi")
    fun submitList(newList: List<T>?) {
        if (newList != mList) {
            val runGeneration = ++mMaxScheduledGeneration

            when {
                newList == null -> {
                    val countRemoved = this.mList?.size?: 0

                    this.mList = null
                    this.mReadOnlyList = listOf()
                    this.mUpdateCallback.onRemoved(0, countRemoved)
                }
                this.mList == null -> {
                    this.mList = newList
                    this.mReadOnlyList = Collections.unmodifiableList(newList)
                    this.mUpdateCallback.onInserted(0, newList.size)
                }
                else -> {
                    val oldList = this.mList

                    this.mConfig.backgroundThreadExecutor.execute {
                        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                            override fun getOldListSize(): Int {
                                return oldList?.size?: 0
                            }

                            override fun getNewListSize(): Int {
                                return newList.size
                            }

                            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                                val oldItem = oldList?.get(oldItemPosition)
                                val newItem = newList[newItemPosition]
                                return if (oldItem != null) {
                                    mConfig.diffCallback.areItemsTheSame(oldItem, newItem)
                                } else {
                                    false
                                }
                            }

                            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                                val oldItem = oldList?.get(oldItemPosition)
                                val newItem = newList[newItemPosition]
                                return if (oldItem != null) {
                                    if (mConfig.diffCallback is NestedItemCallback && !(mConfig.diffCallback as NestedItemCallback<T>).areNestedContentsTheSame(oldItem, newItem) ) {
                                        mList = newList
                                        mReadOnlyList = Collections.unmodifiableList(newList)

                                        if (adapter is BaseNestedListAdapter<T, *, *>) {
                                            adapter.changePayloadOf(oldItemPosition)
                                        }
                                    }

                                    mConfig.diffCallback.areContentsTheSame(oldItem, newItem)
                                } else {
                                    false
                                }
                            }

                            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                                val oldItem = oldList?.get(oldItemPosition)
                                val newItem = newList[newItemPosition]

                                return if (oldItem != null) {
                                    mConfig.diffCallback.getChangePayload(oldItem, newItem)
                                } else {
                                    throw AssertionError()
                                }
                            }
                        })

                        mConfig.mainThreadExecutor?.execute {
                            if (mMaxScheduledGeneration == runGeneration) {
                                latchList(newList, result)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getCurrentList(): List<T> {
        return this.mReadOnlyList
    }

    private fun latchList(newList: List<T>?, diffResult: DiffUtil.DiffResult) {
        this.mList = newList
        this.mReadOnlyList = Collections.unmodifiableList(newList)
        diffResult.dispatchUpdatesTo(this.mUpdateCallback)
    }
}