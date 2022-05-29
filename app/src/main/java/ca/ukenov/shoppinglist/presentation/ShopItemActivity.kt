package ca.ukenov.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ca.ukenov.shoppinglist.R

class ShopItemActivity : AppCompatActivity() {

    private val shopItemViewModel: ShopItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
    }
}