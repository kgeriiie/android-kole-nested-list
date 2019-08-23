package hu.kole.nestedlist

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseNestedListAdapter<TMainData, TNestedData, TViewHolder: RecyclerView.ViewHolder>(diffCallback: NestedItemCallback<TMainData>): ListAdapter<TMainData, TViewHolder>(diffCallback) {

    private var mRecyclerView: RecyclerView? = null
    @Suppress("LeakingThis")
    private val mHelper: NestedAsyncListDiffer<TMainData> = NestedAsyncListDiffer(this, diffCallback)
    val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    fun changePayloadOf(position: Int) {
        val viewHolder = mRecyclerView?.findViewHolderForAdapterPosition(position)

        if (viewHolder is BaseNestedViewHolder<*, *>) {
            (viewHolder as BaseNestedViewHolder<TMainData, TNestedData>).nestedListAdapter.submitList(getNestedItems(position))
        }
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

    abstract class BaseNestedViewHolder<TMainData, TNestedData>(view: View): RecyclerView.ViewHolder(view) {
        abstract val nestedListAdapter: ListAdapter<TNestedData, *>
        abstract fun setup(position: Int, section: TMainData?)
    }
}