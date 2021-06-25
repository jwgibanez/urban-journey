package io.github.jwgibanez.catfacts.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import org.jetbrains.annotations.NotNull;

import io.github.jwgibanez.catfacts.database.model.CatFact;
import io.github.jwgibanez.catfacts.view.ItemViewHolder;
import io.github.jwgibanez.catfacts.viewmodel.CatsViewModel;

public class ListAdapter extends androidx.recyclerview.widget.ListAdapter<CatFact, ItemViewHolder> {

    private final CatsViewModel viewModel;

    public ListAdapter(
            @NonNull CatsViewModel viewModel,
            @NonNull DiffUtil.ItemCallback<CatFact> diffCallback
    ) {
        super(diffCallback);
        this.viewModel = viewModel;
    }

    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return ItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        CatFact current = getItem(position);
        holder.bind(current, viewModel);
    }

    static class Diff extends DiffUtil.ItemCallback<CatFact> {

        @Override
        public boolean areItemsTheSame(@NonNull CatFact oldItem, @NonNull CatFact newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull CatFact oldItem, @NonNull CatFact newItem) {
            return oldItem.url.equals(newItem.url);
        }
    }
}
