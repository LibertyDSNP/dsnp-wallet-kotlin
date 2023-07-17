package com.unfinished.account.presentation.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import coil.ImageLoader
import com.unfinished.account.databinding.ViewChainChipBinding
import com.unfinished.account.presentation.model.chain.ChainUi
import com.unfinished.account.presentation.model.chain.loadChainIcon
import com.unfinished.common.di.FeatureUtils
import com.unfinished.common.utils.setDrawableEnd
import com.unfinished.common.utils.setTextColorRes
import com.unfinished.common.view.shape.getRoundedCornerDrawable

class ChainChipModel(
    val chainUi: ChainUi,
    val changeable: Boolean
)

class ChainChipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var binding: ViewChainChipBinding

    private val imageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        FeatureUtils.getCommonApi(context).imageLoader()
    }

    init {
        binding = ViewChainChipBinding.inflate(LayoutInflater.from(context),this)

        binding.itemAssetGroupLabel.background = context.getRoundedCornerDrawable(com.unfinished.common.R.color.chips_background, cornerSizeInDp = 8)
    }

    fun setModel(chainChipModel: ChainChipModel) {
        setChain(chainChipModel.chainUi)
        setChangeable(chainChipModel.changeable)
    }

    fun setChangeable(changeable: Boolean) {
        binding.itemAssetGroupLabel.isEnabled = changeable

        if (changeable) {
            binding.itemAssetGroupLabel.setTextColorRes(com.unfinished.common.R.color.button_text_accent)
            binding.itemAssetGroupLabel.setDrawableEnd(com.unfinished.common.R.drawable.ic_chevron_down, widthInDp = 16, paddingInDp = 4, tint = com.unfinished.common.R.color.icon_accent)
        } else {
            binding.itemAssetGroupLabel.setTextColorRes(com.unfinished.common.R.color.text_primary)
            binding.itemAssetGroupLabel.setDrawableEnd(null)
        }
    }

    fun setChain(chainUi: ChainUi) {
        binding.itemAssetGroupLabel.text = chainUi.name
        binding.itemAssetGroupLabelIcon.loadChainIcon(chainUi.icon, imageLoader)
    }
}
