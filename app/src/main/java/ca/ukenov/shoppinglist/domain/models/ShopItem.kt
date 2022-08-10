package ca.ukenov.shoppinglist.domain.models

data class ShopItem(
    val title: String,
    val count: Int,
    val isActive: Boolean,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
