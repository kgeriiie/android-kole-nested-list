package hu.kole.test.java.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import hu.kole.test.R;
import hu.kole.test.data.Item;
import hu.kole.test.databinding.ViewItemBinding;

public class ItemAdapter extends ListAdapter<Item, ItemViewHolder> {
    protected ItemAdapter() {
        super(new ItemDiffCallback());
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder((ViewItemBinding) DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.view_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
        Item item = getItem(position);
        if (item != null) {
            itemViewHolder.bind(item);
        }
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    private ViewItemBinding binding;

    public ItemViewHolder(@NonNull ViewItemBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }

    public void bind(Item item) {
        binding.setItem(item);
        binding.executePendingBindings();
    }
}

class ItemDiffCallback extends DiffUtil.ItemCallback<Item> {

    @Override
    public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
        return oldItem.getTitle().equalsIgnoreCase(newItem.getTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
        return oldItem.getTitle().equalsIgnoreCase(newItem.getTitle());
    }
}