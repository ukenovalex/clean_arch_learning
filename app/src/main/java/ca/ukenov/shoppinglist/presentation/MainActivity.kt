package ca.ukenov.shoppinglist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ca.ukenov.shoppinglist.R
import ca.ukenov.shoppinglist.databinding.ActivityMainBinding
import ca.ukenov.shoppinglist.presentation.ShopListAdapter.Companion.DISABLED
import ca.ukenov.shoppinglist.presentation.ShopListAdapter.Companion.ENABLED
import ca.ukenov.shoppinglist.presentation.ShopListAdapter.Companion.MAX_POOL_SIZE
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnFinishShopItemFragment {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        adapter = ShopListAdapter()
        binding.rvShopList.adapter = adapter
        with(binding.rvShopList) {
            recycledViewPool.setMaxRecycledViews(ENABLED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(DISABLED, MAX_POOL_SIZE)
        }
        setupOnLongClickListenerForRecycleView()
        setupOnClickListenerForRecycleView()
        setupOnSwipeToDeleteForRecycleView(binding.rvShopList)
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

    override fun finishShopItemFragment() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

}