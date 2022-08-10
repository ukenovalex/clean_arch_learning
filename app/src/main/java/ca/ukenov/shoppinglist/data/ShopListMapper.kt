package ca.ukenov.shoppinglist.data

import ca.ukenov.shoppinglist.domain.models.ShopItem

class ShopListMapper {
    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        isActive = shopItem.isActive,
        title = shopItem.title,
        count = shopItem.count
    )
    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        isActive = shopItemDbModel.isActive,
        title = shopItemDbModel.title,
        count = shopItemDbModel.count
    )
    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}