package ca.ukenov.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.ukenov.shoppinglist.R
import ca.ukenov.shoppinglist.domain.models.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnFinishShopItemFragment {
    private var screenMode = MODE_UNKNOWN
    private var itemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        setContentView(R.layout.activity_shop_item)

        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(itemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Mode $screenMode is unknown")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }


    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Mode $mode is unknown")
        }
        screenMode = mode
        if (mode == MODE_EDIT && !intent.hasExtra(EDIT_ITEM_ID)) {
            throw RuntimeException("Param id is absent")
        }
        itemId = intent.getIntExtra(EDIT_ITEM_ID, ShopItem.UNDEFINED_ID)
    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "EXTRA_SCREEN_MODE"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"
        private const val EDIT_ITEM_ID = "EDIT_ITEM_ID"
        private const val MODE_UNKNOWN = ""

        fun getAddIntent(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun getEditIntent(context: Context, id: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EDIT_ITEM_ID, id)
            return intent
        }
    }

    override fun finishShopItemFragment() {
        finish()
    }
}