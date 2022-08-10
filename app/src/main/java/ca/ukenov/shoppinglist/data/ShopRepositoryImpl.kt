package ca.ukenov.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.repository.ShopRepository

class ShopRepositoryImpl(
    application: Application
) : ShopRepository {

    private val shopListDao = AppDataBase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()


    override suspend fun addItem(item: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(item))
    }

    override suspend fun deleteItem(item: ShopItem) {
        shopListDao.deleteShopItem(item.id)
    }

    override suspend fun editItem(item: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(item))
    }

    override suspend fun toggleIsActiveItem(item: ShopItem) {
        // TODO FIX IT
        val changedItem = ShopItem(
            title = item.title,
            count = item.count,
            isActive = !item.isActive,
            id = item.id
        )
        shopListDao.addShopItem(mapper.mapEntityToDbModel(changedItem))
    }

    override suspend fun getById(id: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(id)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getItemList(): LiveData<List<ShopItem>> =
        Transformations.map(shopListDao.getShopList()) {
            mapper.mapListDbModelToListEntity(it)
        }

}