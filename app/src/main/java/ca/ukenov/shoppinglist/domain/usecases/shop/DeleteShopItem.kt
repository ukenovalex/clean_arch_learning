package ca.ukenov.shoppinglist.domain.usecases.shop

import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.repository.ShopRepository

class DeleteShopItem(private val repository: ShopRepository) {

    suspend fun deleteItem(item: ShopItem) {
        repository.deleteItem(item)
    }

}