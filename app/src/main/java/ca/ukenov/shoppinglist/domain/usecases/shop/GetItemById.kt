package ca.ukenov.shoppinglist.domain.usecases.shop

import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.repository.ShopRepository

class GetItemById(private val repository: ShopRepository) {

    fun getById(id: Int): ShopItem {
        return repository.getById(id)
    }

}