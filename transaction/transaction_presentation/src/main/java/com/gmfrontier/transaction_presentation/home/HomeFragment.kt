package com.gmfrontier.transaction_presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.util.UiText
import com.gmfrontier.transaction_domain.model.TransactionModel
import com.gmfrontier.transaction_domain.repository.QUERY_PAGE_SIZE
import com.gmfrontier.transaction_presentation.R
import com.gmfrontier.transaction_presentation.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private var transactionsAdapter: TransactionAdapter? = null

    private val stateObserver = Observer<HomeState> { state ->
        state.isSearching?.consume()?.let { showLoading(it) }
        state.transactions?.consume()?.let { updateTransactionsList(it) }
        state.showMessage?.consume()?.let { showMessage(it) }
        state.scrollToTop?.consume()?.let { scrollToTop() }
    }

    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }

    private fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }

    private fun updateTransactionsList(transactionList: List<TransactionModel>) {
        isLoading = false
        if (transactionList.isNotEmpty()) {
            transactionsAdapter?.submitList(transactionList.filter { it.isVisible })
        }
    }

    private fun showLoading(it: Boolean) {
        isLoading = it
        binding.progressBar.isVisible = it
    }

    private fun showMessage(message: UiText) {
        Snackbar.make(binding.root, message.asString(requireContext()), Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUiComponents()
    }

    private fun setupUiComponents() {
        binding.btnSearch.isEnabled = false
        binding.progressBar.isVisible = false
        binding.btnSearch.setOnClickListener {
            viewModel.onEvent(HomeEvent.OnSearch(binding.etSearch.text?.trim().toString()))
            binding.etSearch.setText("")
        }
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = group.findViewById(checkedId)
            if (chip != null) {
                viewModel.onEvent(HomeEvent.OnFilterList(chip.tag.toString()))
            } else {
                viewModel.onEvent(HomeEvent.OnFilterList(""))
            }
        }
        binding.btnSortDate.setOnClickListener { sortList() }
        setupLeafAdapter()
    }

    fun sortList() {
        binding.btnSortDate.isActivated = !binding.btnSortDate.isActivated
        viewModel.onEvent(HomeEvent.OnSortByDateSwitch(binding.btnSortDate.isActivated))
    }

    private fun setupLeafAdapter() {
        val adapterVal = TransactionAdapter()
        transactionsAdapter = adapterVal
        with(binding.recyclerView) {
            adapter = adapterVal
            transactionsAdapter?.submitList(emptyList())
            addOnScrollListener(scrollListener)
        }
    }

    private fun setupViewModel() {
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val token = remoteConfig.getString("token")
                    binding.btnSearch.isEnabled = true
                    viewModel.onEvent(HomeEvent.OnTokenArrived(token))
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.error_fetching_token),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        viewModel.state.observe(viewLifecycleOwner, stateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var isLoading = false
    var isScrolling = false
    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !viewModel.isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeggining = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage &&
                    isAtLastItem &&
                    isNotAtBeggining &&
                    isTotalMoreThanVisible &&
                    isScrolling
            if (shouldPaginate) {
                viewModel.onEvent(HomeEvent.OnPaginate(totalItemCount))
                isScrolling = false
            }
        }
    }
}