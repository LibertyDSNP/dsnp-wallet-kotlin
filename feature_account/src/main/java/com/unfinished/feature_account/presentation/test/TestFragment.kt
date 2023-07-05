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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.unfinished.feature_account.data.secrets.AccountSecretsFactory
import com.unfinished.feature_account.databinding.FragmentTestBinding
import com.unfinished.feature_account.domain.account.advancedEncryption.AdvancedEncryption
import com.unfinished.feature_account.domain.model.AddAccountType
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.domain.model.toUnit
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.utils.setOnSafeClickListener
import io.novafoundation.nova.common.validation.validationError
import io.novafoundation.nova.core.model.CryptoType
import io.novafoundation.nova.runtime.ext.addressOf
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
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let { chain ->
                        viewModel.executeEventBlock(chain)
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
        }
        binding.fireStateGetMetaData.setOnSafeClickListener {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let { chain ->
                        viewModel.getMetaData(chain).let { metdata ->
                            val result = java.lang.StringBuilder()
                            result.append("Metadata: ${metdata.metadata.runtimeVersion}")
                            binding.stateGetMetaData.setText(result.toString())
                        }
                    }
                }
            }
        }
        binding.fireRuntimeVersion.setOnSafeClickListener {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let { chain ->
                        viewModel.getRuntimeVersion(chain).let { runtimeVersions ->
                            val result = java.lang.StringBuilder()
                            result.append("specVersion: ${runtimeVersions.specVersion}")
                                .append("\n")
                            result.append("transactionVersion: ${runtimeVersions.transactionVersion}")
                            binding.runtimeVersion.setText(result.toString())
                        }
                    }
                }
            }
        }
        binding.fireChainGetBlock.setOnSafeClickListener {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let { chain ->
                        viewModel.getBlock(chain).let { block ->
                            val result = java.lang.StringBuilder()
                            result.append("header.parentHash: ${block.block.header.parentHash}")
                            binding.chainGetBlock.setText(result.toString())
                        }
                    }
                }
            }
        }
        binding.fireBlockHash.setOnSafeClickListener {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let { chain ->
                        viewModel.getGenesisHash(chain).let { block ->
                            val result = java.lang.StringBuilder()
                            result.append("blockHash: ${block}")
                            binding.chainGetBlockHash.setText(result.toString())
                        }
                    }
                }
            }
        }

        binding.balanceAccount.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let { chain ->
                        viewModel.testTransfer(
                            chain = chain,
                            enteredAmount = binding.balance.text.toString().toFloat(),
                            paymentInfo = { feeResponse ->
                                Log.e(
                                    "Fee",
                                    "Fee for this transaction ${feeResponse.partialFee} UNIT"
                                )
                            }
                        ).collectLatest {
                            if (it.second.isNullOrEmpty()) {
                                binding.transferResult.setText(it.first)
                            } else {
                                binding.transferResult.setText(
                                    it.second ?: "Invalid Transaction"
                                )
                                Toast.makeText(
                                    requireContext(),
                                    it.second ?: "Invalid Transaction",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let { chain ->
                        viewModel.createMsa(
                            chain = chain,
                            paymentInfo = { feeResponse ->
                                Log.e(
                                    "Fee",
                                    "Fee for this transaction ${feeResponse.partialFee} UNIT"
                                )
                            }
                        ).catch {
                            binding.createMsa.setText(it.message ?: "Invalid Transaction")
                        }.collectLatest {
                            if (it.third.isNullOrEmpty()) {
                                val builer = java.lang.StringBuilder()
                                builer.append("Event: ${it.second?.name}").append("\n")
                                builer.append("Publick Key: ${it.second?.value?.value?.key}")
                                    .append("\n")
                                builer.append("Msa ID: ${it.second?.value?.value?.msa_id}")
                                    .append("\n")
                                builer.append("Block Hash: ${it.first}").append("\n")
                                binding.createMsa.setText(builer.toString())
                            } else {
                                binding.createMsa.setText(it.third ?: "Error create msa id")
                                Toast.makeText(
                                    requireContext(),
                                    it.third ?: "Error create msa id",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
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

        //binding.chainUrl.setText("wss://0.rpc.frequency.xyz")
        binding.setChainUrl.setOnSafeClickListener {
            if (binding.chainUrl.text.toString().isEmpty()) {
                validationError("Set chain url")
                return@setOnSafeClickListener
            }
            Toast.makeText(requireContext(), "Connection Established", Toast.LENGTH_SHORT).show()
            viewModel.setUpNewConnection(binding.chainUrl.text.toString().trim())
        }

        binding.newKeyOwnerAccount.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val account = metaAccounts[position]
                    viewModel.getChain()?.let {
                        binding.newPublicKey.setText("newPublicKey: ${it.addressOf(account.substratePublicKey!!)}")
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.firePublicKeyToMsa.setOnSafeClickListener {
            if (!isAccountsExists()) return@setOnSafeClickListener
            if (binding.msaId.text.toString().isBlank()) {
                validationError("MsaId is missing")
                return@setOnSafeClickListener
            }
            if (binding.msaId.text.toString().isBlank()) {
                validationError("Expiration is missing")
                return@setOnSafeClickListener
            }
            val msaOwnerMetaAccount = metaAccounts[binding.msaOwnerAccount.selectedItemPosition]
            val newKeyOwnerMetaAccount =
                metaAccounts[binding.newKeyOwnerAccount.selectedItemPosition]
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let { chain ->
                        val msaId = binding.msaId.text.toString().toBigInteger()
                        val expiration = binding.expiration.text.toString().toBigInteger()
                        viewModel.addKeyToMsa(
                            chain = chain,
                            msaId = msaId,
                            expiration = expiration,
                            msaOwnerMetaAccount = msaOwnerMetaAccount,
                            newKeyOwnerMetaAccount = newKeyOwnerMetaAccount,
                            paymentInfo = { feeResponse ->
                                Log.e(
                                    "Fee",
                                    "Fee for this transaction ${feeResponse.partialFee} UNIT"
                                )
                            }
                        ).collectLatest {
                            if (it.second.isNullOrEmpty()) {
                                val builer = java.lang.StringBuilder()
                                builer.append("Block Hash: ${it.first}").append("\n")
                                binding.createMsa.setText(builer.toString())
                            } else {
                                binding.createMsa.setText(it.second ?: "Error add public key msa")
                                Toast.makeText(
                                    requireContext(),
                                    it.second ?: "Error add public key msa",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        binding.fireChainPublicKeyToMsaId.setOnSafeClickListener {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let {
                        viewModel.getPublicKeyToMsaId(
                            it,
                            metaAccounts[binding.publicKeyToMsaId.selectedItemPosition]
                        ).collectLatest {
                            if (it.second == null) {
                                binding.publicKeyToMsaIdTv.setText("${it.first ?: "No Msa Id found"}")
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    it.second ?: "Error get msa id",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        binding.fireChainDeletePublicKeyToMsaId.setOnSafeClickListener {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.getChain()?.let {
                        viewModel.deletePublicKeyToMsaId(
                            chain = it,
                            metaAccount = metaAccounts[binding.deleteOwnerPublicKeyToMsaId.selectedItemPosition],
                            newMetaAccount = metaAccounts[binding.deletePublicKeyToMsaId.selectedItemPosition],
                            paymentInfo = { feeResponse ->
                                Log.e("Fee", "Fee for this transaction ${feeResponse.partialFee} UNIT")
                            }
                        ).collectLatest {
                            if (it.second == null) {
                                binding.deletePublicKeyToMsaIdTv.setText("${it.first ?: "No Msa Id found"}")
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    it.second ?: "Error delete public key to msa id",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
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
        binding.msaOwnerAccount.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        binding.newKeyOwnerAccount.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        binding.publicKeyToMsaId.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        binding.deletePublicKeyToMsaId.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        binding.deleteOwnerPublicKeyToMsaId.adapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
    }

    private fun isAccountExists() = viewModel.getMetaAccounts().isNotEmpty()

    private fun isAccountsExists() = viewModel.getMetaAccounts().size > 1

    override fun subscribe(viewModel: TestViewModel) {}

    fun createMetaAccount() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
                }
            }
        }.invokeOnCompletion {
            Log.e("exception", it?.message ?: "Error occured")
        }
    }

}