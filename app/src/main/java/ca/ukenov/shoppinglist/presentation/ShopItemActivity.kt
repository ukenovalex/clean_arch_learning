package ca.ukenov.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ca.ukenov.shoppinglist.databinding.ActivityShopItemBinding
import ca.ukenov.shoppinglist.domain.models.ShopItem

class ShopItemActivity : AppCompatActivity() {
    private val shopItemViewModel: ShopItemViewModel by viewModels()
    private lateinit var binding: ActivityShopItemBinding
    private var screenMode = MODE_UNKNOWN
    private var itemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        setupViewEditShopItem()
        addErrorInputListener()

        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val count = binding.etCount.text.toString()
            shopItemViewModel.editShopItem(inputName = name, inputCount = count)
        }
        onFinishActivity()
    }

    private fun launchAddMode() {
        addErrorInputListener()
        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val count = binding.etCount.text.toString()
            shopItemViewModel.addShopItem(inputName = name, inputCount = count)
        }
        onFinishActivity()
    }

    private fun addErrorInputListener() {
        binding.etName.addTextChangedListener(ClearError {
            shopItemViewModel.resetErrorInputName()
        })
        binding.etCount.addTextChangedListener(ClearError {
            shopItemViewModel.resetErrorInputCount()
        })
        shopItemViewModel.errorInputName.observe(this) {
            if (it) {
                binding.tilName.error = "Name is require field"
            } else {
                binding.tilName.error = ""
            }
        }
        shopItemViewModel.errorInputCount.observe(this) {
            if (it) {
                binding.tilCount.error = "Count is must be more than zero"
            } else {
                binding.tilCount.error = ""
            }
        }
    }

    private fun setupViewEditShopItem() {
        shopItemViewModel.getShopItem(itemId)
        shopItemViewModel.currentShopItem.observe(this) {
            binding.etName.setText(it.title)
            binding.etCount.setText(it.count.toString())
        }
    }

    private fun onFinishActivity() {
        shopItemViewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private class ClearError(val callback: () -> Unit) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback()
        }

        override fun afterTextChanged(s: Editable?) {}
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
}