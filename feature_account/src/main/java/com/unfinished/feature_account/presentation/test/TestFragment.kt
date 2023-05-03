package com.unfinished.feature_account.presentation.test

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.unfinished.feature_account.data.secrets.AccountSecretsFactory
import com.unfinished.feature_account.databinding.FragmentTestBinding
import com.unfinished.feature_account.domain.account.advancedEncryption.AdvancedEncryption
import com.unfinished.feature_account.domain.model.AddAccountType
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.domain.model.addressIn
import com.unfinished.feature_account.domain.model.toUnit
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.data.network.runtime.binding.bindAccountInfo
import io.novafoundation.nova.common.data.secrets.v2.KeyPairSchema
import io.novafoundation.nova.common.data.secrets.v2.MetaAccountSecrets
import io.novafoundation.nova.common.utils.setOnSafeClickListener
import io.novafoundation.nova.common.validation.validationError
import io.novafoundation.nova.core.model.CryptoType
import io.novafoundation.nova.runtime.ext.addressOf
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class TestFragment : BaseFragment<TestViewModel>() {

    override val viewModel: TestViewModel by viewModels()
    lateinit var binding: FragmentTestBinding
    var metaAccounts: List<MetaAccount> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        updateAdapters()
        binding.accounts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val account = metaAccounts[position]
                viewModel.setSelectedAccount(account)
                viewModel.getChain()?.let {
                    binding.accountAddress.setText(it.addressOf(account.substrateAccountId!!))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
        binding.fireStorageQuery.setOnSafeClickListener {
            if (!isAccountExists()) return@setOnSafeClickListener
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    viewModel.executeGetStorageRequest(chain)?.let { accountInfo ->
                        val result = java.lang.StringBuilder()
                        result.append("free: ${accountInfo.data.free}").append("\n")
                        result.append("feeFrozen: ${accountInfo.data.feeFrozen}").append("\n")
                        result.append("miscFrozen: ${accountInfo.data.miscFrozen}").append("\n")
                        result.append("reserved: ${accountInfo.data.reserved}").append("\n")
                        binding.storageQuery.setText(result.toString())
                    }
                }
            }
        }
        binding.fireStateGetMetaData.setOnSafeClickListener {
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    viewModel.getMetaData(chain).let { metdata ->
                        val result = java.lang.StringBuilder()
                        result.append("Metadata: ${metdata.metadata.runtimeVersion}")
                        binding.stateGetMetaData.setText(result.toString())
                    }
                }
            }
        }
        binding.fireRuntimeVersion.setOnSafeClickListener {
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    viewModel.getRuntimeVersion(chain).let { runtimeVersions ->
                        val result = java.lang.StringBuilder()
                        result.append("specVersion: ${runtimeVersions.specVersion}").append("\n")
                        result.append("transactionVersion: ${runtimeVersions.transactionVersion}")
                        binding.runtimeVersion.setText(result.toString())
                    }
                }
            }
        }
        binding.fireChainGetBlock.setOnSafeClickListener {
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    viewModel.getBlock(chain).let { block ->
                        val result = java.lang.StringBuilder()
                        result.append("header.parentHash: ${block.block.header.parentHash}")
                        binding.chainGetBlock.setText(result.toString())
                    }
                }
            }
        }
        binding.fireBlockHash.setOnSafeClickListener {
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    viewModel.getGenesisHash(chain).let { block ->
                        val result = java.lang.StringBuilder()
                        result.append("blockHash: ${block}")
                        binding.chainGetBlockHash.setText(result.toString())
                    }
                }
            }
        }

        binding.balanceAccount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val account = metaAccounts[position]
                viewModel.setSelectedAccountForBalance(account)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        binding.fireBalance.setOnSafeClickListener {
            if (!isAccountExists()) return@setOnSafeClickListener
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    viewModel.checkAccountDetails(chain)?.let { accountInfo ->
                        val result = java.lang.StringBuilder().apply {
                            append("Token: ${"%.4f".format(accountInfo.data.free.toUnit())} UNIT\n")
//                            append("Reserved:${accountInfo.data.reserved}\n")
//                            append("MiscFrozen:${accountInfo.data.miscFrozen}\n")
//                            append("FeeFrozen:${accountInfo.data.feeFrozen}")
                        }.toString()
                      binding.balanceResult.setText(result)
                    } ?: kotlin.run {
                        binding.balanceResult.setText("Token: 0.0000 UNIT")
                    }
                }
            }
        }

        binding.toAccount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val account = metaAccounts[position]
                viewModel.setSelectedTransferAccount(account)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
        binding.fireTransferAmount.setOnSafeClickListener {
            if (!isAccountsExists()) return@setOnSafeClickListener
            if (binding.balance.text.toString().isBlank()) {
                validationError("Amount is missing")
                return@setOnSafeClickListener
            }
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    viewModel.testTransfer(chain, binding.balance.text.toString().toFloat()).collectLatest {
                        it.onSuccess {
                            binding.transferResult.setText(it.first)
                            it.second.onSuccess {
                                Toast.makeText(requireContext(),it.first,Toast.LENGTH_SHORT).show()

                            }.onFailure {
                                if (it.message.equals("List is empty.")){
                                    Toast.makeText(requireContext(),"Extrinsic Success!",Toast.LENGTH_SHORT).show()
                                }else {
                                    Toast.makeText(
                                        requireContext(),
                                        it.message ?: "Extrinsic Failed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }.onFailure {
                            binding.transferResult.setText(it.message ?: "Invalid Transaction")
                            Toast.makeText(requireContext(),it.message ?: "Invalid Transaction",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.msaIdAccount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val account = metaAccounts[position]
                viewModel.setSelectedAccountForMsa(account)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
        binding.fireCreateMsaId.setOnSafeClickListener {
            if (!isAccountExists()) return@setOnSafeClickListener
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    viewModel.createMsa(chain).catch {
                        binding.createMsa.setText(it.message ?: "Invalid Transaction")
                    }.collectLatest {
                        it.onSuccess {
                            binding.createMsa.setText(it.first)
                            it.second.onSuccess {
                                Toast.makeText(requireContext(),it.first,Toast.LENGTH_SHORT).show()

                            }.onFailure {
                                if (it.message.equals("List is empty")){
                                    Toast.makeText(requireContext(),"Extrinsic Success!",Toast.LENGTH_SHORT).show()
                                }else {
                                    Toast.makeText(
                                        requireContext(),
                                        it.message ?: "Extrinsic Failed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }                            }
                        }.onFailure {
                            binding.createMsa.setText(it.message ?: "Error create msa id")
                         Toast.makeText(requireContext(),it.message ?: "Error create msa id",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.fireCreateMsaIdSubmit.setOnSafeClickListener {
            if (!isAccountExists()) return@setOnSafeClickListener
            lifecycleScope.launchWhenResumed {
                viewModel.getChain()?.let { chain ->
                    withContext(Dispatchers.IO){
                        viewModel.executeAnyExtrinsic(chain)
                    }
                }
            }
        }
        binding.createAccount.setOnSafeClickListener {
            if (binding.accountName.text.toString().isBlank()) {
                validationError("Wallet name is missing")
                return@setOnSafeClickListener
            }
            if (binding.importMnemonicContent.text.toString().isBlank()) {
                validationError("Mnemonic is missing")
                return@setOnSafeClickListener
            }
            createMetaAccount()
        }

        binding.fireEventInfo.setOnSafeClickListener {
            viewModel.getChain()?.let {
                lifecycleScope.launchWhenResumed {
                    withContext(Dispatchers.IO){
                        viewModel.executeEventBlock(it)
                        viewModel.getEvents(it)
                    }
                }
            }
        }

        //binding.chainUrl.setText("wss://0.rpc.frequency.xyz")
        binding.setChainUrl.setOnSafeClickListener {
            if (binding.chainUrl.text.toString().isEmpty()) {
                validationError("Set chain url")
                return@setOnSafeClickListener
            }
            Toast.makeText(requireContext(),"Connection Established",Toast.LENGTH_SHORT).show()
            viewModel.setUpNewConnection(binding.chainUrl.text.toString().trim())
        }
    }

    private fun updateAdapters() {
        metaAccounts = viewModel.getMetaAccounts()
        val list = metaAccounts.map { it.name }
        binding.accounts.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        binding.msaIdAccount.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        binding.toAccount.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        binding.balanceAccount.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
    }

    private fun isAccountExists() = viewModel.getMetaAccounts().isNotEmpty()

    private fun isAccountsExists() = viewModel.getMetaAccounts().size > 1

    override fun subscribe(viewModel: TestViewModel) {}

    fun createMetaAccount() {
        lifecycleScope.launchWhenResumed {
            viewModel.createAccount(
                derivationPaths = AdvancedEncryption.DerivationPaths("", "//44//60//0/0/0"),
                addAccountType = AddAccountType.MetaAccount(binding.accountName.text.toString()),
                accountSource = AccountSecretsFactory.AccountSource.Mnemonic(
                    CryptoType.SR25519,
                    binding.importMnemonicContent.text.toString()
                )
            ) { result ->
                Toast.makeText(requireContext(), "Account created!", Toast.LENGTH_SHORT).show()
                updateAdapters()
                when (result.first) {
                    true -> {
                        val data = result.second
                        data?.let { secrets ->
                            //public keys
                            val substratePublicKey =
                                secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PublicKey]
                            val ethereumPublicKey =
                                secrets[MetaAccountSecrets.EthereumKeypair]?.get(KeyPairSchema.PublicKey)
                            //private keys
                            val substratePrivateKey =
                                secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PrivateKey]
                            val ethereumPrivateKey =
                                secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PrivateKey]

                        }
                    }
                    false -> {
                        val data = result.third
                        data?.let { secrets ->
                            //public keys
                            val substratePublicKey =
                                secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PublicKey]
                            val ethereumPublicKey =
                                secrets[MetaAccountSecrets.EthereumKeypair]?.get(KeyPairSchema.PublicKey)
                            //private keys
                            val substratePrivateKey =
                                secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PrivateKey]
                            val ethereumPrivateKey =
                                secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PrivateKey]
                        }
                    }
                }
            }
        }.invokeOnCompletion {
            Log.e("exception", it?.message ?: "Error occured")
        }
    }

}