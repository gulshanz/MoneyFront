package com.gulshan.moneyfront.ui.mf_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.gulshan.moneyfront.MainActivity
import com.gulshan.moneyfront.R
import com.gulshan.moneyfront.data.Mf
import com.gulshan.moneyfront.databinding.FragmentMfListBinding
import com.gulshan.moneyfront.ui.mf_list.adapter.MfAdapter
import com.gulshan.moneyfront.ui.mf_list.adapter.MfLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MfListFragment : Fragment(R.layout.fragment_mf_list), MfAdapter.OnItemClickListener {
    private val viewModel by viewModels<MfListViewModel>()
    private var _binding: FragmentMfListBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("testLifecyle", "onViewCreated: ")
        _binding = FragmentMfListBinding.bind(view)

        val adapter = MfAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MfLoadStateAdapter { adapter.retry() },
                footer = MfLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
            viewModel.mfs.observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            adapter.addLoadStateListener { loadState ->
                binding.apply {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                    buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                    textViewError.isVisible = loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1
                    ) {
                        recyclerView.isVisible = false
                        textViewEmpty.isVisible = true
                    } else {
                        textViewEmpty.isVisible = false
                    }
                }
            }
        }

    }

    override fun onItemClick(mf: Mf) {
        val bundle = bundleOf("name" to mf.Scheme_Name,"family" to mf.Mutual_Fund_Family)
        findNavController().navigate(R.id.action_mfList_to_mfDeatails, bundle )
    }

    override fun onDestroy() {
        Log.d("testLifecyle", "onDestroy: ")
        super.onDestroy()
        _binding = null
    }


}