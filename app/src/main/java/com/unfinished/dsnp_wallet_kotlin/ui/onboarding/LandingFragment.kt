package com.unfinished.dsnp_wallet_kotlin.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.unfinished.dsnp_wallet_kotlin.util.createComposeView
import com.unfinished.account.presentation.model.account.add.AddAccountPayload
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.mixin.impl.observeBrowserEvents

@AndroidEntryPoint
class LandingFragment : BaseFragment<LandingViewModel>() {

    override val viewModel by activityViewModels<LandingViewModel>()

    companion object {
        private const val KEY_DISPLAY_BACK = "display_back"
        private const val KEY_ADD_ACCOUNT_PAYLOAD = "add_account_payload"

        fun bundle(displayBack: Boolean): Bundle {
            return Bundle().apply {
                putBoolean(KEY_DISPLAY_BACK, displayBack)
                //putParcelable(KEY_ADD_ACCOUNT_PAYLOAD, AddAccountPayload.MetaAccount)
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
    ): View = requireContext().createComposeView {
//        LandingPageScreen(
//            landingViewModel = viewModel,
//            testScreenClick = {
//                findNavController().navigate(R.id.action_landingFragment_to_testFragment)
//            }
//        )
    }

    override fun initViews() {

    }

    override fun subscribe(viewModel: LandingViewModel) {
        observeBrowserEvents(viewModel)
        viewModel.setBundleArguments(argument(KEY_DISPLAY_BACK),argument(KEY_ADD_ACCOUNT_PAYLOAD))
    }
}