package hu.kole.test.java.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import hu.kole.nestedlist.BaseNestedListAdapter;
import hu.kole.nestedlist.NestedItemCallback;
import hu.kole.test.R;
import hu.kole.test.data.Item;
import hu.kole.test.data.Section;
import hu.kole.test.databinding.ViewSectionBinding;

public class SectionAdapter extends BaseNestedListAdapter<Section, Item, BaseNestedListAdapter.BaseNestedViewHolder<Section, Item>> {
    public SectionAdapter() {
        super(new SectionDiffCallback());
    }

    @NotNull
    @Override
    public List<Item> getNestedItems(int position) {
        return getItem(position).getData();
    }

    @NonNull
    @Override
    public BaseNestedViewHolder<Section, Item> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new SectionViewHolder((ViewSectionBinding) DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.view_section, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseNestedViewHolder<Section, Item> holder, int position) {
        holder.setup(position, getItem(position));
    }
}

class SectionViewHolder extends BaseNestedListAdapter.BaseNestedViewHolder<Section, Item> {
    private ViewSectionBinding mBinding;
    private ItemAdapter mNestedAdapter;

    public SectionViewHolder(@NotNull ViewSectionBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    @NotNull
    @Override
    public ListAdapter<Item, ?> getNestedListAdapter() {
        if (mNestedAdapter == null) {
            mNestedAdapter = new ItemAdapter();
            mNestedAdapter.setHasStableIds(true);
        }

        return mNestedAdapter;
    }

    @Override
    public void setup(int position, @Nullable Section section) {
        if (section != null) {
            SimpleItemAnimator animator = (SimpleItemAnimator) mBinding.recyclerView.getItemAnimator();

            if (animator != null) {
                animator.setSupportsChangeAnimations(false);
            }

            mBinding.recyclerView.setHasFixedSize(true);

            mBinding.recyclerView.setAdapter(getNestedListAdapter());
            mBinding.setSection(section);
            mBinding.executePendingBindings();

            this.getNestedListAdapter().submitList(section.getData());
        }
    }
}

class SectionDiffCallback extends NestedItemCallback<Section> {

    @Override
    public boolean areNestedContentsTheSame(Section oldSection, Section newSection) {
        return oldSection.getData().equals(newSection.getData());
    }

    @Override
    public boolean areItemsTheSame(@NonNull Section oldSection, @NonNull Section newSection) {
        return oldSection.equals(newSection);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Section oldSection, @NonNull Section newSection) {
        return oldSection.equals(newSection);
    }
}