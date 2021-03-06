package com.paviotti.crudapplication

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.paviotti.crudapplication.databinding.ActivityMainBinding
import android.content.DialogInterface
import android.util.Log


/**
 * Aula do professor Wagner Machado do Amaral
 * https://www.youtube.com/watch?v=hRptcOy1g2M&list=PLK9MWsE3ev06gntM1UQme9_ltjxCeUiew&index=5
 * */
class MainActivity : AppCompatActivity() {
    private lateinit var bancoDados: SQLiteDatabase
    private lateinit var binding: ActivityMainBinding
    private lateinit var arrayIds: ArrayList<Int>
    private var idSelecionado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //vai para a tela cadastrar
        binding.btnCadastrar.setOnClickListener {
            abrirTelaCadastro()
        }

        //exclui o registrto ao dar um click longo
        binding.listViewDados.setOnItemLongClickListener(OnItemLongClickListener { adapterView, view, i, l ->
            idSelecionado = arrayIds.get(i)
            confirmaExcluir()
            true
        })

        //altera o registro ao dar um click simples
        binding.listViewDados.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            idSelecionado =arrayIds.get(i)
            abrirTelaAlterar()
        })

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
            arrayIds = ArrayList() //vai ser usado em excluir()
            bancoDados =
                openOrCreateDatabase("crudapp", MODE_PRIVATE, null) //faz a conex??o com o banco
            val meuCursor: Cursor
            meuCursor = bancoDados.rawQuery("""SELECT id, nome From coisa""", null)
            val linhas = arrayListOf<String>()
            binding.listViewDados.adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, linhas)
            meuCursor.moveToFirst()
            while (meuCursor != null) {
                linhas.add(meuCursor.getString(1))
                arrayIds.add(meuCursor.getInt(0)) //posi????o zero por conta de ser um array
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

    private fun excluir() {
        try {
            bancoDados =
                openOrCreateDatabase("crudapp", MODE_PRIVATE, null) //faz a conex??o com o banco
            val sql = """DELETE FROM coisa WHERE id=?"""
            val stmt: SQLiteStatement  = bancoDados.compileStatement(sql)
            stmt.bindLong(1, idSelecionado.toLong()) //bindLong() porque ?? inteiro e 1 porque s?? tem um item
            stmt.executeUpdateDelete()
            listarDados()
            bancoDados.close()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }
    }
    private fun confirmaExcluir(){
        val msgBox = AlertDialog.Builder(this@MainActivity)
        msgBox.setTitle("Excluir")
        msgBox.setIcon(R.drawable.ic_menu_delete)
        msgBox.setMessage("Voc?? realmente deseja excluir esse registro?")
        msgBox.setPositiveButton(
            "Sim"
        ) { dialogInterface, i ->
            excluir()
            listarDados()
        }
        msgBox.setNegativeButton(
            "N??o"
        ) { dialogInterface, i -> }
        msgBox.show()
    }

    fun abrirTelaAlterar() {
        val intent = Intent(this, AlterarActivity::class.java)
        intent.putExtra("id", idSelecionado)

       // Log.d("idSelec ", "Valor: $idSelecionado")
        startActivity(intent)
    }
}



