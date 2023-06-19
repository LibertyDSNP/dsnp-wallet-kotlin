package io.novafoundation.nova.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.address.AddressModel
import io.novafoundation.nova.common.utils.WithContextExtensions
import io.novafoundation.nova.common.utils.makeGone
import io.novafoundation.nova.common.utils.makeVisible
import io.novafoundation.nova.common.utils.setDrawableEnd

class AddressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle), WithContextExtensions {

    override val providedContext: Context = context

    private val view = View.inflate(context, R.layout.view_address, this)
    private val addressImage: ImageView = view.findViewById(R.id.addressImage)
    private val addressValue: TextView = view.findViewById(R.id.addressValue)

    init {

        setEndIcon(R.drawable.ic_info_cicrle_filled_16)
    }

    fun setAddress(icon: Drawable, address: String) {
        addressImage.setImageDrawable(icon)
        addressValue.text = address
    }

    fun setEndIcon(@DrawableRes iconRes: Int?) {
        if (iconRes == null) {
            addressValue.setDrawableEnd(null)
        } else {
            addressValue.setDrawableEnd(iconRes, widthInDp = 16, tint = R.color.text_secondary, paddingInDp = 6)
        }
    }
}

fun AddressView.setAddressModel(addressModel: AddressModel) {
    setAddress(addressModel.image, addressModel.nameOrAddress)
}

fun AddressView.setAddressOrHide(addressModel: AddressModel?) {
    if (addressModel == null) {
        makeGone()
        return
    }

    makeVisible()

    setAddress(addressModel.image, addressModel.nameOrAddress)
}
