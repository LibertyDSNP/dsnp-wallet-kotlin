package com.unfinished.dsnp_wallet_kotlin.ui.onboarding

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Placeholder
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentLandingBinding
import com.unfinished.dsnp_wallet_kotlin.util.createSpannable
import com.unfinished.dsnp_wallet_kotlin.util.showBrowser
import com.unfinished.dsnp_wallet_kotlin.util.toast
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.utils.setOnSafeClickListener
import io.novafoundation.nova.common.R as commonR

@AndroidEntryPoint
class LandingFragment : Fragment() {

    lateinit var binding: FragmentLandingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.landingCreateDsnpId.setOnSafeClickListener {
            toast("In Progress")
        }
        binding.landingDsnpId.setOnSafeClickListener {
            findNavController().navigate(R.id.action_landingFragment_to_lookupFragment)
        }

        configureTermsAndPrivacy(
            sourceText = getString(R.string.landing_privacy_policy),
            terms = getString(R.string.terms),
            privacy = getString(R.string.privacy_policy)
        )
    }

    private fun configureTermsAndPrivacy(sourceText: String, terms: String, privacy: String) {
        binding.landingPrivacyPolicy.apply {
            linksClickable = true
            isClickable = true
            movementMethod = LinkMovementMethod.getInstance()
            text = createSpannable(
                content = sourceText,
                typeface = ResourcesCompat.getFont(requireContext(),commonR.font.poppins_semibold),
                highlightTextColor = ContextCompat.getColor(requireContext(),commonR.color.orange))  {
                clickable(terms) {
                    showBrowser(getString(R.string.terms_link))
                }
                clickable(privacy) {
                    showBrowser(getString(R.string.privacy_policy_link))
                }
            }
        }
    }


}