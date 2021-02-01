package com.example.isen_2021.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.isen_2021.R
import com.example.isen_2021.basket.Basket
import com.example.isen_2021.basket.BasketItem
import com.example.isen_2021.databinding.ActivityDetailBinding
import com.example.isen_2021.network.Dish
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlin.math.max

class DetailActivity : AppCompatActivity() {
    companion object {
        const val DISH_EXTRA = "DISH_EXTRA"
    }

    lateinit var binding: ActivityDetailBinding
    private var itemCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dish = intent.getSerializableExtra(DISH_EXTRA) as? Dish
        dish?.let {
            setupView(it)
        }
    }

    private fun setupView(dish: Dish) {
        binding.dishTitleTextView.text = dish.name
        binding.ingredientTextView.text = dish.ingredients.map { it.name }?.joinToString(", ")
        binding.viewPager.adapter = PhotoAdapter(this, dish.images)
        refreshShop(dish)

        binding.less.setOnClickListener {
            itemCount = max(1, itemCount - 1)
            refreshShop(dish)
        }

        binding.more.setOnClickListener {
            itemCount += 1
            refreshShop(dish)
        }

        binding.shopButton.setOnClickListener {
            addToBasket(dish, itemCount)
        }
    }

    private fun refreshShop(dish: Dish) {
        val price = itemCount * dish.prices.first().price.toFloat()
        binding.itemCount.text = itemCount.toString()
        binding.shopButton.text = "${getString(R.string.total)} $price€"
    }

    private fun addToBasket(dish: Dish, count: Int) {
        val basket = Basket.getBasket(this)
        basket.addItem(BasketItem(dish, count))
        basket.save(this)
        val json = GsonBuilder().create().toJson(basket)
        Log.d("basket", json)
    }
}