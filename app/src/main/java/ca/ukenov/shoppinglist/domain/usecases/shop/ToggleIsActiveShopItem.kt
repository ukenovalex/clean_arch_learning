package ca.ukenov.shoppinglist.domain.usecases.shop

import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.repository.ShopRepository

class ToggleIsActiveShopItem(private val repository: ShopRepository) {

    suspend fun toggleIsActiveItem(item: ShopItem) {
        repository.toggleIsActiveItem(item)
    }
}