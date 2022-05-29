package ca.ukenov.shoppinglist.domain.usecases.shop

import androidx.lifecycle.LiveData
import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.repository.ShopRepository

class GetShopItemList(private val repository: ShopRepository) {

    fun getItemList(): LiveData<List<ShopItem>> {
        return repository.getItemList()
    }

}