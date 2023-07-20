package com.unfinished.account.presentation.test

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.unfinished.account.data.secrets.AccountSecretsFactory
import com.unfinished.account.databinding.FragmentTestBinding
import com.unfinished.account.domain.advancedEncryption.AdvancedEncryption
import com.unfinished.account.domain.model.AddAccountType
import com.unfinished.data.model.account.MetaAccount
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.utils.setOnSafeClickListener
import com.unfinished.common.validation.validationError
import com.unfinished.data.model.CryptoType
import com.unfinished.common.utils.toUnit
import com.unfinished.data.util.ext.addressOf
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        loadAdapters()
    }

    private fun loadAdapters() {
        metaAccounts = viewModel.getMetaAccounts()
        val list = metaAccounts.map { it.name }
        binding.spAccountList.setAdapterWithList(list)
        binding.spToTransferAccount.setAdapterWithList(list)
        binding.spForAccountBalance.setAdapterWithList(list)
        binding.spForCreateMsa.setAdapterWithList(list)
        binding.spForMsaOwner.setAdapterWithList(list)
        binding.spForNewKeyOwner.setAdapterWithList(list)
        binding.spForFindMsa.setAdapterWithList(list)
        binding.spForDeleteOwnerMsa.setAdapterWithList(list)
        binding.spForDeleteKeyOwnerMsa.setAdapterWithList(list)
        binding.spForRetireMsa.setAdapterWithList(list)
        binding.spForCreateProvider.setAdapterWithList(list)
    }

    private fun Spinner.setAdapterWithList(list: List<String>) {
        adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, list)
    }

    private fun isAccountExists() = viewModel.getMetaAccounts().isNotEmpty()

    private fun isAccountsExists() = viewModel.getMetaAccounts().size > 1

    override fun subscribe(viewModel: TestViewModel) {
        viewModel.getChain()?.let { chain ->
            binding.spAccountList.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val account = metaAccounts[position]
                        binding.tvAccountAddress.text =
                            chain.addressOf(account.substrateAccountId!!)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}

                }

            binding.btnRuntimeMetadata.setOnSafeClickListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.getRuntimeMetaData(chain).let { metdata ->
                            val result = java.lang.StringBuilder()
                            result.append("Metadata: ${metdata.metadata.runtimeVersion}")
                            binding.tvRuntimeMetadata.text = result.toString()
                        }
                    }
                }
            }
            binding.btnRuntimeVersion.setOnSafeClickListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.getRuntimeVersion(chain).let { runtimeVersions ->
                            val result = java.lang.StringBuilder()
                            result.append("specVersion: ${runtimeVersions.specVersion}")
                                .append("\n")
                            result.append("transactionVersion: ${runtimeVersions.transactionVersion}")
                            binding.tvRuntimeVersion.text = result.toString()
                        }
                    }
                }
            }
            binding.btnChainGetBlock.setOnSafeClickListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.getBlock(chain).let { block ->

                        }
                    }
                }
            }
            binding.btnBlockHash.setOnSafeClickListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.getGenesisHash(chain).let { block ->
                            val result = java.lang.StringBuilder()
                            result.append("blockHash: ${block}")
                            binding.tvChainGetBlockHash.text = result.toString()
                        }
                    }
                }
            }

            binding.btnCreateAccount.setOnSafeClickListener {
                if (binding.etNewAccountName.text.toString().isBlank()) {
                    validationError("Wallet name is missing")
                    return@setOnSafeClickListener
                }
                if (binding.etMnemonicPhrase.text.toString().isBlank()) {
                    validationError("Mnemonic is missing")
                    return@setOnSafeClickListener
                }
                createMetaAccount(
                    name = binding.etNewAccountName.text.toString(),
                    mnemonic = binding.etMnemonicPhrase.text.toString()
                )
            }

            binding.btnAccountBalance.setOnSafeClickListener {
                if (!isAccountExists()) return@setOnSafeClickListener
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.checkAccountDetails(
                            chain = chain,
                            metaAccount = metaAccounts[binding.spForAccountBalance.selectedItemPosition]
                        ).collectLatest { accountInfo ->
                            val result = java.lang.StringBuilder().apply {
                                append("Token: ${"%.4f".format(accountInfo?.data?.free?.toUnit())} UNIT\n")
//                            append("Reserved:${accountInfo.data.reserved}\n")
//                            append("MiscFrozen:${accountInfo.data.miscFrozen}\n")
//                            append("FeeFrozen:${accountInfo.data.feeFrozen}")
                            }.toString()
                            binding.tvAccountBalance.text = result
                        }
                    }
                }
            }

            binding.btnTransferAmount.setOnSafeClickListener {
                if (!isAccountsExists()) return@setOnSafeClickListener
                if (binding.etBalance.text.toString().isBlank()) {
                    validationError("Amount is missing")
                    return@setOnSafeClickListener
                }
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.testTransfer(
                            chain = chain,
                            amount = binding.etBalance.text.toString().toFloat(),
                            metaAccount = metaAccounts[binding.spAccountList.selectedItemPosition],
                            destMetaAccount = metaAccounts[binding.spToTransferAccount.selectedItemPosition]
                        ).collectLatest { result ->
                            result.onSuccess {
                                binding.tvTransferBalanceResult.setText("Success")
                            }.onFailure {
                                Toast.makeText(
                                    requireContext(),
                                    it.message ?: "Invalid Transaction",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            binding.btnCreateMsaId.setOnSafeClickListener {
                if (!isAccountExists()) return@setOnSafeClickListener
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.createMsa(
                            chain = chain,
                            metaAccount = metaAccounts[binding.spForCreateMsa.selectedItemPosition]
                        ).collectLatest { result ->
                            result.onSuccess {
                                val builer = java.lang.StringBuilder()
                                builer.append("Event: ${it.msaEvent?.name}").append("\n")
                                builer.append("Publick Key: ${it.msaEvent?.value?.value?.key}")
                                    .append("\n")
                                builer.append("Msa ID: ${it.msaEvent?.value?.value?.msa_id}")
                                binding.tvCreateMsa.text = builer.toString()
                            }.onFailure {
                                binding.tvCreateMsa.text = it.message ?: "Error create msa id"
                                Toast.makeText(
                                    requireContext(),
                                    it.message ?: "Error create msa id",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            binding.spForNewKeyOwner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val account = metaAccounts[position]
                        binding.tvPublicKeyToMsaValue.text =
                            "newPublicKey: ${chain.addressOf(account.substratePublicKey!!)}"
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

            binding.btnPublicKeyToMsa.setOnSafeClickListener {
                if (!isAccountsExists()) return@setOnSafeClickListener
                if (binding.etMsaId.text.toString().isBlank()) {
                    validationError("MsaId is missing")
                    return@setOnSafeClickListener
                }
                if (binding.etExpiration.text.toString().isBlank()) {
                    validationError("Expiration is missing")
                    return@setOnSafeClickListener
                }
                val msaOwnerMetaAccount = metaAccounts[binding.spForMsaOwner.selectedItemPosition]
                val newKeyOwnerMetaAccount = metaAccounts[binding.spForNewKeyOwner.selectedItemPosition]
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        val msaId = binding.etMsaId.text.toString().toBigInteger()
                        val expiration = binding.etExpiration.text.toString().toBigInteger()
                        viewModel.addKeyToMsa(
                            chain = chain,
                            msaId = msaId,
                            expiration = expiration,
                            msaOwnerMetaAccount = msaOwnerMetaAccount,
                            newKeyOwnerMetaAccount = newKeyOwnerMetaAccount
                        ).collectLatest { result ->
                            result.onSuccess {
                                binding.tvPublicKeyToMsaValue.text = "Success"
                            }.onFailure {
                                binding.tvPublicKeyToMsaValue.text = it.message ?: "Error add public key msa"
                                Toast.makeText(
                                    requireContext(),
                                    it.message ?: "Error add public key msa",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            binding.btnFindMsa.setOnSafeClickListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.getPublicKeyToMsaId(
                            chain = chain,
                            metaAccount = metaAccounts[binding.spForFindMsa.selectedItemPosition]
                        ).collectLatest { result ->
                            result.onSuccess {
                                binding.tvFindMsa.text = "${it ?: "No Msa Id found"}"
                            }.onFailure {
                                Toast.makeText(
                                    requireContext(),
                                    it.message ?: "Error get msa id",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            binding.btnDeletePublicKeyMsa.setOnSafeClickListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.deletePublicKeyToMsaId(
                            chain = chain,
                            metaAccount = metaAccounts[binding.spForDeleteKeyOwnerMsa.selectedItemPosition],
                            newMetaAccount = metaAccounts[binding.spForDeleteKeyOwnerMsa.selectedItemPosition]
                        ).collectLatest { result ->
                            result.onSuccess {
                                binding.tvDeletePulicKeyMsa.text = "Success"
                            }.onFailure {
                                Toast.makeText(
                                    requireContext(),
                                    it.message ?: "Error delete public key to msa id",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            binding.btnRetireMsa.setOnSafeClickListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.retireMsa(
                            chain = chain,
                            metaAccount = metaAccounts[binding.spForRetireMsa.selectedItemPosition],
                        ).collectLatest { result ->
                            result.onSuccess {
                                binding.tvRetireMsa.text = "Success"
                            }.onFailure {
                                Toast.makeText(
                                    requireContext(),
                                    it.message ?: "Error retire msa",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            binding.btnCreateProvider.setOnSafeClickListener {
                if (binding.etCreateProvider.text.toString().isNullOrEmpty()) {
                    validationError("Provider name is missing")
                    return@setOnSafeClickListener
                }
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.createProvider(
                            chain = chain,
                            metaAccount = metaAccounts[binding.spForCreateProvider.selectedItemPosition],
                            name = binding.etCreateProvider.text.toString()
                        ).collectLatest { result ->
                            result.onSuccess {
                                binding.tvCreateProvider.setText("Success")
                            }.onFailure {
                                Toast.makeText(
                                    requireContext(),
                                    it.message ?: "Error create provider",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    fun createMetaAccount(
        name: String,
        mnemonic: String
    ) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.createAccount(
                    mnemonic = mnemonic,
                    advancedEncryption = AdvancedEncryption(
                        substrateCryptoType = CryptoType.SR25519,
                        ethereumCryptoType = CryptoType.SR25519,
                        derivationPaths = AdvancedEncryption.DerivationPaths("", "//44//60//0/0/0")
                    ),
                    addAccountType = AddAccountType.MetaAccount(name),
                ).onSuccess {
                    Toast.makeText(requireContext(), "Account created!", Toast.LENGTH_SHORT).show()
                    loadAdapters()
                }.onFailure {

                }
            }
        }.invokeOnCompletion {
            Log.e("exception", it?.message ?: "Error occured")
        }
    }

}