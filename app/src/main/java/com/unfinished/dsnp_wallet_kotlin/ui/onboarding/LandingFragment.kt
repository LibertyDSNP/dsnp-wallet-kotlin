package com.unfinished.dsnp_wallet_kotlin.ui.onboarding

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentLandingBinding
import com.unfinished.dsnp_wallet_kotlin.util.createSpannable
import com.unfinished.dsnp_wallet_kotlin.util.showBrowser
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.mixin.impl.observeBrowserEvents
import io.novafoundation.nova.common.utils.setOnSafeClickListener
import io.novafoundation.nova.common.R as commonR

@AndroidEntryPoint
class LandingFragment : BaseFragment<LandingViewModel>() {

    override val viewModel by activityViewModels<LandingViewModel>()
    lateinit var binding: FragmentLandingBinding

    companion object {
        private const val KEY_DISPLAY_BACK = "display_back"
        private const val KEY_ADD_ACCOUNT_PAYLOAD = "add_account_payload"

        fun bundle(displayBack: Boolean): Bundle {
            return Bundle().apply {
                putBoolean(KEY_DISPLAY_BACK, displayBack)
                putParcelable(KEY_ADD_ACCOUNT_PAYLOAD, AddAccountPayload.MetaAccount)
            }
        }

        fun bundle(payload: AddAccountPayload): Bundle {
            return Bundle().apply {
                putBoolean(KEY_DISPLAY_BACK, true)
                putParcelable(KEY_ADD_ACCOUNT_PAYLOAD, payload)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        binding.landingCreateDsnpId.setOnSafeClickListener {
            viewModel.openLookupScreen()
        }
        binding.landingDsnpId.setOnSafeClickListener {
            viewModel.importAccountClicked()
        }

        configureTermsAndPrivacy(
            sourceText = getString(R.string.landing_privacy_policy),
            terms = getString(R.string.terms),
            privacy = getString(R.string.privacy_policy)
        )
    }

    override fun subscribe(viewModel: LandingViewModel) {
        observeBrowserEvents(viewModel)
        viewModel.setBundleArguments(argument(KEY_DISPLAY_BACK),argument(KEY_ADD_ACCOUNT_PAYLOAD))
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