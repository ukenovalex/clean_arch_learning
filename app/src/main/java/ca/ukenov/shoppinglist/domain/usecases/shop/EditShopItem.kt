package ca.ukenov.shoppinglist.domain.usecases.shop

import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.repository.ShopRepository

class EditShopItem(private val repository: ShopRepository) {

    fun editItem(item: ShopItem) {
        repository.editItem(item)
    }

}