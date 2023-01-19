package com.unfinished.dsnp_wallet_kotlin.ui.onboarding

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ccodepicker.Country
import com.unfinished.dsnp_wallet_kotlin.ccodepicker.CountryCodeSheet
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentLookupBinding
import com.unfinished.dsnp_wallet_kotlin.util.*


class LookupFragment : Fragment() {

    lateinit var binding: FragmentLookupBinding
    private var selectedCountry: Country? = null
    private var isEmail: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputField.requestFocus()
        binding.inputField.showSoftKeyboard()
        binding.toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (checkedId == binding.lookupEmailBtn.id){
                binding.inputFieldSwitcher.displayedChild = 0
                binding.lookupEmailBtn.setBackgroundTintColorId(R.color.button_color)
                binding.lookupPhoneBtn.setBackgroundTintColorId(R.color.text_black)
            }else {
                binding.inputFieldSwitcher.displayedChild = 1
                binding.lookupPhoneBtn.setBackgroundTintColorId(R.color.button_color)
                binding.lookupEmailBtn.setBackgroundTintColorId(R.color.text_black)
            }
        }

        binding.countryCode.setOnClickListener {
           val countryCodeSheet =  CountryCodeSheet()
            countryCodeSheet.onSetDismissListener {
                this.selectedCountry = it
                binding.countryCode.text = it.dial_code
            }
            countryCodeSheet.show(childFragmentManager,"ccodepicker_sheet")
        }

        binding.sendAuthCode.setOnClickListener {
            if (validation()){
                if (binding.phoneNo.text.toString().startsWith("0000")){
                    showPhoneErrorUI()
                }else if (binding.inputField.text.toString().startsWith("test")){
                    showEmailErrorUI()
                }else {
                    showVerifyCodeFragment()
                }
            }
        }

        binding.tryAgain.setOnClickListener {
            showDefaultUI()
        }

        configureTermsAndPrivacy(
            sourceText = getString(R.string.landing_privacy_policy),
            terms = getString(R.string.terms),
            privacy = getString(R.string.privacy_policy)
        )
    }

    private fun showDefaultUI(){
        binding.textinputError.setText(getText(R.string.lookup_temp_error))
        binding.textinputError.hide()
        binding.countryCodeBaseline.setBackgroundTintColorId(R.color.input_field_line)
        binding.phoneBaseline.setBackgroundTintColorId(R.color.input_field_line)
        binding.countryCodeBaseline.setBackgroundTintColorId(R.color.input_field_line)
        binding.phoneBaseline.setBackgroundTintColorId(R.color.input_field_line)
        binding.buttonSwitcher.displayedChild = 0
    }

    private fun showPhoneErrorUI(){
        binding.textinputError.setText(getText(R.string.lookup_temp_error))
        binding.textinputError.show()
        binding.countryCodeBaseline.setBackgroundTintColorId(R.color.orange)
        binding.phoneBaseline.setBackgroundTintColorId(R.color.orange)
        binding.buttonSwitcher.displayedChild = 1
    }

    private fun showEmailErrorUI(){
        binding.textinputError.setText(getText(R.string.lookup_temp_error))
        binding.textinputError.show()
        binding.emailBaseline.setBackgroundTintColorId(R.color.orange)
        binding.buttonSwitcher.displayedChild = 1
    }

    private fun validation(): Boolean {
        var isValid = true
        if (binding.inputFieldSwitcher.displayedChild == 0) {
            isEmail = true
            if (binding.inputField.text.isNullOrEmpty()) {
                isValid = false
                toast(getString(R.string.enter_email))
            } else {
                if (!binding.inputField.text.toString().isValidEmail()) {
                    isValid = false
                    toast(getString(R.string.invalid_email))
                }
            }
        }else {
            isEmail = false
            if (binding.phoneNo.text.isNullOrEmpty()) {
                isValid = false
                toast(getString(R.string.enter_phone))
            }
        }
        return isValid
    }

    private fun configureTermsAndPrivacy(sourceText: String, terms: String, privacy: String) {
        binding.lookupPrivacyPolicy.apply {
            linksClickable = true
            isClickable = true
            movementMethod = LinkMovementMethod.getInstance()
            text = createSpannable(
                content = sourceText,
                typeface = ResourcesCompat.getFont(requireContext(),R.font.poppins_semibold),
                highlightTextColor = ContextCompat.getColor(requireContext(),R.color.orange))  {
                clickable(terms) {
                    showBrowser(getString(R.string.terms_link))
                }
                clickable(privacy) {
                    showBrowser(getString(R.string.privacy_policy_link))
                }
            }
        }
    }

    private fun showVerifyCodeFragment(){
        val text = if (isEmail) binding.inputField.text.toString()
        else binding.phoneNo.text.toString()
        val icon = if (isEmail) R.drawable.baseline_email_24
        else R.drawable.baseline_smartphone_24
        val verifyCodeFragment= VerifyCodeFragment(text,icon)
        verifyCodeFragment.setDismissListener {
            if (it){
                findNavController().navigate(R.id.action_lookupFragment_to_seedPhraseFragment)
            }
        }
        verifyCodeFragment.show(childFragmentManager,"verify_code_frag")
    }
}