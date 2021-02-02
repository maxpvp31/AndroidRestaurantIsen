package com.example.isen_2021.basket

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.isen_2021.R
import com.example.isen_2021.databinding.ActivityBasketBinding
import com.example.isen_2021.detail.DetailViewFragment
import com.example.isen_2021.registration.RegisterFragment
import com.example.isen_2021.registration.UserActivity

class BasketActivity : AppCompatActivity(), BasketCellInterface {
    lateinit var binding: ActivityBasketBinding
    private lateinit var basket: Basket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        basket = Basket.getBasket(this)

        val fragment = BasketItemsFragment(basket, this)
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit()

        binding.orderButton.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivityForResult(intent, UserActivity.REQUEST_CODE)
        }
    }

    override fun onDeleteItem(item: BasketItem) {
        basket.items.remove(item)
        basket.save(this)
    }

    override fun onShowDetail(item: BasketItem) {
        val fragment = DetailViewFragment(item.dish)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == UserActivity.REQUEST_CODE) {
            val sharedPreferences = getSharedPreferences(UserActivity.USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val idUser = sharedPreferences.getInt(UserActivity.ID_USER, -1)
            if(idUser != -1) {
                sendOrder(idUser)
            }
        }
    }

    private fun sendOrder(idUser: Int) {
        val message = basket.items.map { "${it.count}x ${it.dish.name}" }.joinToString("\n")
        //basket.clear()
    }
}