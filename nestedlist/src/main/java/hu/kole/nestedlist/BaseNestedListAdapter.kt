package hu.kole.nestedlist

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseNestedListAdapter<TMainData, TNestedData, TNestedViewHolder: BaseNestedListAdapter.BaseNestedViewHolder<TNestedData, TMainData>>(diffCallback: NestedItemCallback<TMainData>): ListAdapter<TMainData, TNestedViewHolder>(diffCallback) {

    private var mRecyclerView: RecyclerView? = null
    @Suppress("LeakingThis")
    private val mHelper: NestedAsyncListDiffer<TMainData> = NestedAsyncListDiffer(this, diffCallback)
    val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    fun changePayloadOf(position: Int) {
        findViewHolderForAdapterPosition(position)?.submitList(getNestedItems(position))
    }

    @Suppress("UNCHECKED_CAST")
    private fun findViewHolderForAdapterPosition(position: Int): BaseNestedViewHolder<TNestedData, TMainData>? {
        return mRecyclerView?.findViewHolderForAdapterPosition(position) as? TNestedViewHolder
    }

    override fun submitList(list: List<TMainData>?) {
        mHelper.submitList(list)
    }

    override fun getItem(position: Int): TMainData {
        return mHelper.getCurrentList()[position]
    }

    abstract fun getNestedItems(position: Int): List<TNestedData>

    override fun getItemCount(): Int {
        return mHelper.getCurrentList().size
    }

    abstract class BaseNestedViewHolder<in TNestedData, in TMainData>(view: View): RecyclerView.ViewHolder(view) {
        abstract fun setup(position: Int, section: TMainData?)
        abstract fun submitList(list: List<TNestedData>)
    }
}