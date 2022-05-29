package ca.ukenov.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.repository.ShopRepository
import java.lang.RuntimeException
import kotlin.math.log
import kotlin.random.Random

object ShopRepositoryImpl : ShopRepository {


    private val itemsLiveData = MutableLiveData<List<ShopItem>>()
    private val items = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})
    private var autoIncrementId = 0

    init {
        for (i in 0 until 1000) {
            addItem(
                ShopItem(
                    title = "Title $i",
                    count = Random.nextInt(0, 100),
                    isActive = Random.nextBoolean()
                )
            )
        }
    }


    override fun addItem(item: ShopItem) {
        if(item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        items.add(item)
        updateLiveData()
    }

    override fun deleteItem(item: ShopItem) {
        items.remove(item)
        updateLiveData()
    }

    override fun editItem(item: ShopItem) {
        items.remove(getById(item.id))
        addItem(item)
    }
    override fun toggleIsActiveItem(item: ShopItem) {
        items.remove(item)
        addItem(ShopItem(title = item.title, id = item.id, isActive = !item.isActive, count = item.count))
    }

    override fun getById(id: Int): ShopItem {
        return items.find {
            it.id == id
        } ?: throw RuntimeException("Element with id: $id not found")
    }

    override fun getItemList(): LiveData<List<ShopItem>> {
        return itemsLiveData
    }



    private fun updateLiveData() {
        itemsLiveData.value = items.toList()
    }
}