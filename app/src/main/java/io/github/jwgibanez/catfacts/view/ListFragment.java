package io.github.jwgibanez.catfacts.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.jwgibanez.catfacts.R;
import io.github.jwgibanez.catfacts.databinding.FragmentRecyclerViewBinding;
import io.github.jwgibanez.catfacts.view.ListAdapter;
import io.github.jwgibanez.catfacts.viewmodel.CatsViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@AndroidEntryPoint
public class ListFragment extends Fragment {

    private FragmentRecyclerViewBinding binding;
    private CatsViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentRecyclerViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull @NotNull View view,
            @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CatsViewModel.class);
        viewModel.error.observe(getViewLifecycleOwner(), this::showError);
        viewModel.loading.observe(getViewLifecycleOwner(), this::showLoading);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        final ListAdapter adapter = new ListAdapter(viewModel, new ListAdapter.Diff());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                // Scroll to newly added item
                layoutManager.scrollToPositionWithOffset(positionStart, 0);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                Toast.makeText(getContext(), "Fact deleted.", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.facts.observe(getViewLifecycleOwner(), facts -> {
            binding.emptyMessage.setVisibility(facts.size() > 0 ? GONE : VISIBLE);
            adapter.submitList(facts);
        });

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(
                binding.recyclerView.getContext(), layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(
            @NonNull @NotNull Menu menu,
            @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            viewModel.add(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showError(String message) {
        if (message != null)
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }

}