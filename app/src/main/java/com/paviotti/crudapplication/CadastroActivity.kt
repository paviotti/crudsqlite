package com.paviotti.crudapplication

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.paviotti.crudapplication.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var bancoDados: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCadastrar.setOnClickListener {
            cadastrar(binding.editTxtName.text.toString())

        }
    }

    private fun cadastrar(nome: String) {
       // Log.d("Text: ", "Texto=${nome}")
        when {
            binding.editTxtName.text.isNullOrEmpty() -> {
                binding.editTxtName.error = "Campo vazio, preencha"
            }
            else -> {
                try {
                    bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null)
                    val sql = """INSERT INTO coisa(nome) VALUES(?)"""
                    val stmt: SQLiteStatement = bancoDados.compileStatement(sql)
                    stmt.bindString(1, nome) //recebe da função
                    stmt.executeInsert()
                    bancoDados.close()
                    finish() //finaliza a tela
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}