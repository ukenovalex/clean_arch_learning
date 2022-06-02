package ca.ukenov.shoppinglist.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ca.ukenov.shoppinglist.R
import ca.ukenov.shoppinglist.presentation.ShopListAdapter.Companion.DISABLED
import ca.ukenov.shoppinglist.presentation.ShopListAdapter.Companion.ENABLED
import ca.ukenov.shoppinglist.presentation.ShopListAdapter.Companion.MAX_POOL_SIZE
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: ShopListAdapter
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        shopItemContainer = findViewById(R.id.shop_item_container)
        mainViewModel.items.observe(this) {
            adapter.submitList(it)
        }


        findViewById<FloatingActionButton>(R.id.button_add_shop_item).setOnClickListener {
            println(isOnePanMode())
            if (isOnePanMode()) {
                val intent = ShopItemActivity.getAddIntent(this)
                startActivity(intent)
            } else {
                createShopItemFragment(ShopItemFragment.newInstanceAddItem())
            }
        }

    }

    private fun isOnePanMode(): Boolean {
        return shopItemContainer == null
    }

    private fun createShopItemFragment(fragment: Fragment) {

        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()


    }


    private fun setupRecyclerView() {
        val rvShopList: RecyclerView = findViewById(R.id.rv_shop_list)
        adapter = ShopListAdapter()
        rvShopList.adapter = adapter
        with(rvShopList) {
            recycledViewPool.setMaxRecycledViews(ENABLED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(DISABLED, MAX_POOL_SIZE)
        }
        setupOnLongClickListenerForRecycleView()
        setupOnClickListenerForRecycleView()
        setupOnSwipeToDeleteForRecycleView(rvShopList)


    }

    private fun setupOnLongClickListenerForRecycleView() {
        adapter.onLongClickListener = {
            mainViewModel.toggleIsActive(it)
        }
    }

    private fun setupOnClickListenerForRecycleView() {
        adapter.onClickListener = {
            if (isOnePanMode()) {
                val intent = ShopItemActivity.getEditIntent(this, it.id)
                startActivity(intent)
            } else {
                createShopItemFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupOnSwipeToDeleteForRecycleView(rvShopList: RecyclerView) {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val findItem = adapter.currentList[viewHolder.adapterPosition]
                        mainViewModel.deleteShopItem(findItem)
                    }
                }
            }

        }
        val callback = ItemTouchHelper(simpleCallback)
        callback.attachToRecyclerView(rvShopList)
    }

}