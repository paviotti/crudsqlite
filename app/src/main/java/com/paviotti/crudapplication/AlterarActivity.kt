package com.paviotti.crudapplication


import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.paviotti.crudapplication.databinding.ActivityAlterarBinding
import java.lang.Exception

class AlterarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlterarBinding
    private lateinit var bancoDados: SQLiteDatabase
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlterarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //receber o id da tela anterior
        val intent = getIntent()
        id = intent.getIntExtra("id", 0)
        // Log.d("cursor", "Valor: $id")
        carregarDados()

        binding.btnAlterar.setOnClickListener {
            alterar()
        }
    }


    @SuppressLint("Recycle")
    private fun carregarDados() {
        try {
            bancoDados =
                openOrCreateDatabase("crudapp", MODE_PRIVATE, null) //faz a conexão com o banco
            val cursor: Cursor =
                bancoDados.rawQuery("""SELECT id, nome FROM coisa WHERE id=$id""", null)
            cursor.moveToFirst()
            binding.editTxtName.setText(cursor.getString(1))//pega o nome(1)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun alterar() {
        val valueName = binding.editTxtName.text.toString()
        try {
            val sql = """UPDATE coisa SET nome=? WHERE id=?"""
            val stmt: SQLiteStatement = bancoDados.compileStatement(sql)
            stmt.bindString(1, valueName)
            stmt.bindLong(2, id.toLong())
            stmt.executeUpdateDelete()
            bancoDados.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finish()
    }
}


