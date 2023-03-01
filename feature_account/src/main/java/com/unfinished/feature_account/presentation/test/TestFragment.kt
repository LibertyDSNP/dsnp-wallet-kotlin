package com.unfinished.feature_account.presentation.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.unfinished.feature_account.data.secrets.AccountSecretsFactory
import com.unfinished.feature_account.databinding.FragmentTestBinding
import com.unfinished.feature_account.domain.account.advancedEncryption.AdvancedEncryption
import com.unfinished.feature_account.domain.model.AddAccountType
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.data.secrets.v2.KeyPairSchema
import io.novafoundation.nova.common.data.secrets.v2.MetaAccountSecrets
import io.novafoundation.nova.common.utils.setOnSafeClickListener
import io.novafoundation.nova.core.model.CryptoType

@AndroidEntryPoint
class TestFragment : BaseFragment<TestViewModel>() {

    override val viewModel: TestViewModel by viewModels()
    lateinit var binding: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        binding.fireExtrinsics.setOnSafeClickListener {
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let {
//                    viewModel.executeGetStorageRequest(it)
//                    viewModel.getRuntimeVersion(it)
//                    viewModel.getBlock(it)
//                    viewModel.getGenesisHash(it)
//                    viewModel.getStateRuntimeVersion(it)
                    viewModel.testTransfer(it)
                }
            }
        }
        binding.createAccount.setOnSafeClickListener {
            createMetaAccount()
        }
    }

    override fun subscribe(viewModel: TestViewModel) {}

    fun createMetaAccount(){
        lifecycleScope.launchWhenResumed {
            val result = viewModel.getScretes(
                derivationPaths = AdvancedEncryption.DerivationPaths("","//44//60//0/0/0"),
                addAccountType = AddAccountType.MetaAccount("test"),
                accountSource = AccountSecretsFactory.AccountSource.Mnemonic(CryptoType.SR25519,viewModel.mnemonic)
            )
            when(result.first){
                true -> {
                    val data = result.second
                    data?.let { secrets ->
                        //public keys
                        val substratePublicKey = secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PublicKey]
                        val ethereumPublicKey = secrets[MetaAccountSecrets.EthereumKeypair]?.get(KeyPairSchema.PublicKey)
                        //private keys
                        val substratePrivateKey = secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PrivateKey]
                        val ethereumPrivateKey = secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PrivateKey]

                    }
                }
                false ->  {
                    val data = result.third
                    data?.let { secrets ->
                        //public keys
                        val substratePublicKey = secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PublicKey]
                        val ethereumPublicKey = secrets[MetaAccountSecrets.EthereumKeypair]?.get(KeyPairSchema.PublicKey)
                        //private keys
                        val substratePrivateKey = secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PrivateKey]
                        val ethereumPrivateKey = secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PrivateKey]
                    }
                }
            }
        }
    }

}