package com.dilan.kamuda.customerapp.model.order

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderItemIntermediate(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("image") val image: ByteArray?,
):Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItemIntermediate

        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        return image?.contentHashCode() ?: 0
    }

    fun updateQuantityBy(orderItem: OrderItem) {
        if (this.name == orderItem.name) {
            this.quantity += orderItem.quantity
        }
    }
}
