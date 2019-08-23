package hu.kole.test.adapter

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import hu.kole.nestedlist.BaseNestedListAdapter
import hu.kole.nestedlist.NestedItemCallback
import hu.kole.test.R
import hu.kole.test.data.Item
import hu.kole.test.data.Section
import hu.kole.test.databinding.ViewSectionBinding

class SectionAdapter: BaseNestedListAdapter<Section, Item, BaseNestedListAdapter.BaseNestedViewHolder<Section, Item>>(SectionDiffCallback()) {
    override fun getNestedItems(position: Int): List<Item> {
        return getItem(position).data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseNestedViewHolder<Section, Item> {
        return SectionViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_section, parent, false))
    }

    override fun onBindViewHolder(holder: BaseNestedViewHolder<Section, Item>, position: Int) {
        holder.setup(position, getItem(position))
    }
}

class SectionViewHolder(private val binding: ViewSectionBinding): BaseNestedListAdapter.BaseNestedViewHolder<Section, Item>(binding.root) {
    override val nestedListAdapter: ListAdapter<Item, *> = ItemAdapter().apply { setHasStableIds(true) }

    override fun setup(position: Int, section: Section?) {
        section?.let {
            (binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = nestedListAdapter
            binding.section = section
            binding.executePendingBindings()

            this.nestedListAdapter.submitList(section.data)
        }
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