package com.cookingassistant.data

import android.content.Context
import android.util.Log
import com.cookingassistant.MainActivity
import java.io.FileInputStream
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File
import java.io.FileNotFoundException

@Serializable
data class ShoppingProduct (
    val id : UInt,
    val text : String,
    val isChecked : Boolean,
)

object ShoppingProducts {
    private var _products : MutableList<ShoppingProduct> = mutableListOf()
    private var _lastId = _products.size
    fun addProduct(text : String) {
        _products.add( ShoppingProduct( (_lastId+1).toUInt(), text,false ) )
        //todo save product to file
    }
    fun getProducts() : List<ShoppingProduct> {
        _products.sortBy { product -> product.isChecked }
        return _products
    }
    fun checkProduct(product: ShoppingProduct, isChecked: Boolean) {
        val old = _products[_products.indexOf(product)]
        _products[_products.indexOf(product)] = ShoppingProduct(old.id,old.text,isChecked)
    }
    fun removeProduct(product: ShoppingProduct) {
        _products.remove(product)
    }
    fun loadProducts(context: Context) {
        try {
            _products = Json.decodeFromStream<MutableList<ShoppingProduct>>(
                context.openFileInput("shopping_products.json")
            )
        } catch (e : FileNotFoundException) {
            Log.e("ShoppingProducts", "file doesn't exist")
        }
    }
    fun saveProducts(context: Context) {
        val jsonString = Json.encodeToString(_products)
        val filename = "shopping_products.json"
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }
}