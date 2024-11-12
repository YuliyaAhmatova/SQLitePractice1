package com.example.sqlitepractice1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var listAdapter: ListAdapter? = null
    private var products: MutableList<Product> = mutableListOf()
    private val db = DBHelper(this)

    private lateinit var toolbarMain: Toolbar
    private lateinit var listViewLV: ListView
    private lateinit var saveBTN: Button
    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var costET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        toolbarMain = findViewById(R.id.toolbarMain)
        listViewLV = findViewById(R.id.listViewLV)
        saveBTN = findViewById(R.id.saveBTN)
        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        costET = findViewById(R.id.costET)

        setSupportActionBar(toolbarMain)
        title = "Потребительская корзина"
        toolbarMain.setLogo(R.drawable.baseline_shopping_basket_24)

        saveBTN.setOnClickListener {

            if (nameET.text.isEmpty() || weightET.text.isEmpty() || costET.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Заполнены не все поля",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val product =
                Product(nameET.text.toString(), weightET.text.toString(), costET.text.toString())

            db.addProduct(product)
            products = db.readProduct()
            listAdapter = MyAdapter(this@MainActivity, products)
            listViewLV.adapter = listAdapter
            (listAdapter as MyAdapter)?.notifyDataSetChanged()
            clearEditFields()
        }
    }

    private fun clearEditFields() {
        nameET.text.clear()
        weightET.text.clear()
        costET.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.exitMainMenu -> {
                finishAffinity()
                Toast.makeText(
                    applicationContext,
                    "Программа завершена",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}