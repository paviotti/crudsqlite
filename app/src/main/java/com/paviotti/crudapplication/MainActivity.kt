package com.paviotti.crudapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import com.paviotti.crudapplication.databinding.ActivityMainBinding

/**
 * Aula do professor Wagner Machado do Amaral
 * https://www.youtube.com/watch?v=hRptcOy1g2M&list=PLK9MWsE3ev06gntM1UQme9_ltjxCeUiew&index=5
 * */
class MainActivity : AppCompatActivity() {
    private lateinit var bancoDados: SQLiteDatabase
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastrar.setOnClickListener {
            abrirTelaCadastro()
        }

        criarBancoDados()
        // inserirDadosTemp()
        listarDados()
    }

    /** quando os dados retornarem onResume lista os dados de novo*/
    override fun onResume() {
        super.onResume()
        listarDados()
        Toast.makeText(this, "Executou OnResume", Toast.LENGTH_SHORT).show()
    }

    private fun abrirTelaCadastro() {
        val intent = Intent(this, CadastroActivity::class.java)
        startActivity(intent)
    }

    private fun criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null)
            bancoDados.execSQL("""CREATE TABLE IF NOT EXISTS coisa( id INTEGER PRIMARY KEY AUTOINCREMENT,nome VARCHAR)""")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("Recycle") //por causa do rawQuery
    private fun listarDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null)
            val meuCursor: Cursor
            meuCursor = bancoDados.rawQuery("""SELECT id, nome From coisa""", null)
            val linhas = arrayListOf<String>()
            binding.listViewDados.adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, linhas)
            meuCursor.moveToFirst()
            while (meuCursor != null) {
                linhas.add(meuCursor.getString(1))
                meuCursor.moveToNext()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun inserirDadosTemp() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null)
            val sql = """INSERT INTO coisa(nome) VALUES(?)"""
            val stmt: SQLiteStatement = bancoDados.compileStatement(sql)
            stmt.bindString(1, "Coisa 1")
            stmt.executeInsert()
            stmt.bindString(1, "Coisa abc")
            stmt.executeInsert()
            stmt.bindString(1, "Coisa efg")
            stmt.executeInsert()
            bancoDados.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}



