package io.github.jwgibanez.catfacts.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.github.jwgibanez.catfacts.database.model.CatFact;
import io.github.jwgibanez.catfacts.databinding.ListItemBinding;
import io.github.jwgibanez.catfacts.service.PhotoService;
import io.github.jwgibanez.catfacts.viewmodel.CatsViewModel;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private final ListItemBinding binding;

    private ItemViewHolder(ListItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(CatFact fact, CatsViewModel viewModel) {
        binding.fact.setText(fact.fact != null ? fact.fact : "");
        binding.progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(PhotoService.HOST + fact.url).into(
                binding.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.imageView.setImageDrawable(
                                ContextCompat.getDrawable(
                                        binding.imageView.getContext(),
                                        android.R.drawable.stat_notify_error));
                    }
                });
        itemView.setOnLongClickListener(v -> {
            viewModel.delete(fact);
            return true;
        });
    }

    static ItemViewHolder create(ViewGroup parent) {
        return new ItemViewHolder(
                ListItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false));
    }
}