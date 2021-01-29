package com.example.isen_2021.basket

import com.example.isen_2021.network.Dish
import java.io.Serializable

class Basket (val items: List<BasketItem>): Serializable {}

class BasketItem(val dish: Dish, val count: Int): Serializable {}