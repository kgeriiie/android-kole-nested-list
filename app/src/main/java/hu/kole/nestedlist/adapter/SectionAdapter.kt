package hu.kole.nestedlist.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import hu.kole.nestedlist.BaseNestedListAdapter
import hu.kole.nestedlist.NestedItemCallback
import hu.kole.nestedlist.R
import hu.kole.nestedlist.data.Item
import hu.kole.nestedlist.data.Section
import hu.kole.nestedlist.databinding.ViewSectionBinding

class SectionAdapter: BaseNestedListAdapter<Section, Item, BaseNestedListAdapter.BaseNestedViewHolder<Item, Section>>(SectionDiffCallback()) {
    override fun getNestedItems(position: Int): List<Item> {
        return getItem(position).data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseNestedViewHolder<Item, Section> {
        return SectionViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_section, parent, false))
    }

    override fun onBindViewHolder(holder: BaseNestedViewHolder<Item, Section>, position: Int) {
        holder.setup(position, getItem(position))
    }
}

class SectionViewHolder(private val binding: ViewSectionBinding): BaseNestedListAdapter.BaseNestedViewHolder<Item, Section>(binding.root) {

    val adapter = ItemAdapter().apply {
        setHasStableIds(true)
    }

    override fun setup(position: Int, section: Section?) {
        section?.let {
            (binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = adapter
            binding.section = section
            binding.executePendingBindings()

            this.submitList(section.data)
        }
    }

    override fun submitList(list: List<Item>) {
        adapter.submitList(list)
    }
}

class SectionDiffCallback: NestedItemCallback<Section>() {
    override fun areNestedContentsTheSame(old: Section, new: Section): Boolean {
        return old.data == new.data
    }

    override fun areItemsTheSame(old: Section, new: Section): Boolean {
        return old == new
    }

    override fun areContentsTheSame(old: Section, new: Section): Boolean {
        return old == new
    }
}