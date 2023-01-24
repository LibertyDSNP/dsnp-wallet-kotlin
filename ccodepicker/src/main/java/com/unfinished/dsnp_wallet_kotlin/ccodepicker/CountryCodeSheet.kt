package com.unfinished.dsnp_wallet_kotlin.ccodepicker

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unfinished.dsnp_wallet_kotlin.ccodepicker.databinding.FragmentCountryCodeSheetBinding

class CountryCodeSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCountryCodeSheetBinding
    private var onCustomDismissCallback: ((Country) -> Unit)? = null
    private val countryList: List<Country> by lazy { countryList(requireContext()) }
    private var selectedCountry: Country? = null
    private val countryAdapter: CountryListAdapter by lazy {
        CountryListAdapter(
            onItemClicked = { position, country ->
                selectedCountry = country
                dismiss()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryCodeSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.countryRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countryAdapter
        }
        countryAdapter.updateList(countryList)
        binding.inputField.doAfterTextChanged {
            if (it.toString().isNullOrEmpty()){
                countryAdapter.updateList(countryList)
            }else{
                val filteredList = countryList.searchCountryList(it.toString())
                countryAdapter.updateList(filteredList)
            }
        }
    }

    fun onSetDismissListener(callback: (Country) -> Unit){
        this.onCustomDismissCallback = callback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { setupBottomSheet(it) }
        return dialog
    }

    private fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        selectedCountry?.let { onCustomDismissCallback?.invoke(it) }
    }
}