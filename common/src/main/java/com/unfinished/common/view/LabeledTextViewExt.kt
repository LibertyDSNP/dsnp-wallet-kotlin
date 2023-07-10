package com.unfinished.common.view

import com.unfinished.common.address.AddressModel

fun LabeledTextView.setAddress(addressModel: AddressModel) {
    setTextIcon(addressModel.image)
    setMessage(addressModel.nameOrAddress)
}
