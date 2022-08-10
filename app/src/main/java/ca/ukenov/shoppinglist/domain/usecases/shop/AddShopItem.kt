package ca.ukenov.shoppinglist.domain.usecases.shop

import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.repository.ShopRepository

class AddShopItem(private val repository: ShopRepository) {

    suspend fun addItem(item: ShopItem) {
        repository.addItem(item)
    }
}