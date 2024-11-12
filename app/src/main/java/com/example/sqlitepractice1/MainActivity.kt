package com.example.sqlitepractice1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var listAdapter: MyAdapter? = null
    private var products: MutableList<Product> = mutableListOf()
    private val dataBase = DBHelper(this)

    private lateinit var toolbarMain: Toolbar
    private lateinit var listViewLV: ListView
    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var costET: EditText

    private lateinit var saveBTN: Button
    private lateinit var updateBTN: Button
    private lateinit var deleteBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        setSupportActionBar(toolbarMain)
        title = "Потребительская корзина"
        toolbarMain.setLogo(R.drawable.baseline_shopping_basket_24)

        saveBTN.setOnClickListener {
            saveRecord()
        }
    }

    private fun init() {
        toolbarMain = findViewById(R.id.toolbarMain)
        listViewLV = findViewById(R.id.listViewLV)
        saveBTN = findViewById(R.id.saveBTN)
        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        costET = findViewById(R.id.costET)
        updateBTN = findViewById(R.id.updateBTN)
        deleteBTN = findViewById(R.id.deleteBTN)

        viewDataAdapter()
    }

    override fun onResume() {
        super.onResume()
        updateBTN.setOnClickListener {
            updateRecord()
        }
        deleteBTN.setOnClickListener {
            deleteRecord()
        }
    }

    private fun viewDataAdapter() {
        products = dataBase.readProduct()
        listAdapter = MyAdapter(this, products)
        listViewLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
    }

    private fun checkForEmptiness(name: String, weight: String, cost: String) {
        if (name.trim().isEmpty() || weight.trim().isEmpty() || cost.trim().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Заполнены не все поля",
                Toast.LENGTH_LONG
            ).show()
            return
        }
    }

    private fun clearEditFields() {
        nameET.text.clear()
        weightET.text.clear()
        costET.text.clear()
    }

    private fun saveRecord() {
        checkForEmptiness(nameET.text.toString(), weightET.text.toString(), costET.text.toString())

        val product =
            Product(nameET.text.toString(), weightET.text.toString(), costET.text.toString())

        products.add(product)
        dataBase.addProduct(product)

        viewDataAdapter()
        clearEditFields()
    }

    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val chooseDeleteName = dialogView.findViewById<EditText>(R.id.deleteNameET)
        dialogBuilder.setTitle("Удалить запись")
        dialogBuilder.setMessage("введите имя: ")
        dialogBuilder.setPositiveButton("Удалить") { _, _ ->
            val deleteName = chooseDeleteName.text.toString().trim()
            if (deleteName != "") {
                val product = Product(deleteName, "", "")
                dataBase.deleteProduct(product)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Запись удалена", Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.cancel()
        }
        dialogBuilder.create().show()
    }

    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.updateNameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.updateWeightET)
        val editCost = dialogView.findViewById<EditText>(R.id.updateCostET)

        dialogBuilder.setTitle("Изменить запись")
        dialogBuilder.setMessage("введите данные ниже: ")
        dialogBuilder.setPositiveButton("Изменить") { dialog, _ ->
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updateCost = editCost.text.toString()
            checkForEmptiness(updateName, updateWeight, updateCost)

            val product = Product(updateName, updateWeight, updateCost)
            dataBase.updateProduct(product)
            viewDataAdapter()
            Toast.makeText(applicationContext, "Запись изменена", Toast.LENGTH_LONG).show()
        }
        dialogBuilder.setNegativeButton("Отмена") { dialog, which ->
        }
        dialogBuilder.create().show()
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