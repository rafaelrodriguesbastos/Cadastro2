package com.android.Cadastro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Cadastro extends Activity {
	//Declaracao de Variaveis
	EditText campoNome,
			campoEndereco,
			campoTelefone;

	Button   btnCadastro,
			btnMostra,
			btnCadastrar,
			btnVoltar,
			btnSair,
			btnProximo,
			btnAnterior;

	TextView mostraNome,
			mostraTelefone,
			mostraEndereco;

	SQLiteDatabase banco = null;

	Cursor c;

	int ColunaNome,
			ColunaEndereco,
			ColunaTelefone;


	@SuppressLint("WorldReadableFiles")
	public void abreBanco() {
		try {
			String nmBD = "cadastro";
			// cria o banco de dados caso ele n�o exista
			banco = openOrCreateDatabase(nmBD,
					MODE_WORLD_READABLE,null);
			//cria a tabela no banco de dados caso ela
			//n�o exista
			banco.execSQL("CREATE TABLE IF NOT EXISTS lista "
					+  "(id INTEGER PRIMARY KEY, " +
					"nome TEXT," +
					"endereco TEXT," +
					"telefone TEXT);");
		} catch (Exception e) {
			mostraCxTexto("Criando BD. Mensagem: " +
					e.getMessage(), "ERRO");
		}
	}


	public void fechaBanco() {
		try {
			banco.close();
		} catch (Exception e) {
			mostraCxTexto("Fechando BD. Mensagem: " +
					e.getMessage(), "ERRO");
		}
	}





	protected void mostraRegistro() {
		//associa dados do registro da tabela aos objetos
		//na view
		mostraNome.setText(c.getString(ColunaNome));
		mostraEndereco.setText(c.getString(ColunaEndereco));
		mostraTelefone.setText(c.getString(ColunaTelefone));
	}





	private boolean carregaRegistros() {
		try {
			c = banco.query("lista", //tabela
					new String[]{"nome", "endereco", "telefone"}, //colunas
					null, //Clausula WHERE
					null, //Valores dos argumentos na clausula WHERE
					null, //Clausula GROUP BY
					null, //Clausula HAVING
					null, //Clausula ORDER BY
					null);//Limite de registros retornados
			ColunaNome     = c.getColumnIndex("nome");
			ColunaEndereco = c.getColumnIndex("endereco");
			ColunaTelefone = c.getColumnIndex("telefone");
			// Checa se o resultado � valido
			if (c.getCount() != 0) {
				c.moveToFirst();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			mostraCxTexto("Pesquisando BD. Mensagem: " +
					e.getMessage(), "ERRO");
			return false;
		}
	}




	protected void mostraRegAnterior() {
		try{
			c.moveToPrevious();
			mostraRegistro();
		} catch (Exception e) {
			mostraCxTexto("N�o h� regs anteriores "
					, "AVISO");
		}
	}


	protected void mostraRegProximo() {
		try{
			c.moveToNext();
			mostraRegistro();
		} catch (Exception e) {
			mostraCxTexto("N�o h� regs posteriores "
					, "AVISO");
		}
	}




	protected void insereRegistro() {
		//tenta inserir na tabela lista os dados do
		//registro
		try {
			banco.execSQL("INSERT INTO lista(nome," +
					"endereco," +
					"telefone) " +
					"VALUES ('"+
					campoNome.getText().toString() + "','" +
					campoEndereco.getText().toString() + "','" +
					campoTelefone.getText().toString() + "')");
		} catch (Exception e) {
			mostraCxTexto("Inserindo BD. Mensagem: " +
					e.getMessage(), "ERRO");
		}
	}



	protected void mostraCxTexto(String msg, String
			titulo) {
//Gera uma caixa de texto na tela com um bot�o
		//"OK"
		AlertDialog.Builder builder = new
				AlertDialog.Builder(this);
		builder.setMessage(msg);
		builder.setNeutralButton("OK", null);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titulo);
		dialog.show();
	}

	void iniciaAplicacao() {
//chama o layout principal
		setContentView(R.layout.main);
		fechaBanco();
		inicializaObjetos();
		carregaListeners();
	}

	void iniciaCadastro() {
//muda para o layout de cadastramento
		setContentView(R.layout.cadastro);
		inicializaObjetos();
		carregaListeners();
		campoNome.requestFocus();
	}

	void iniciaMostrarLista() {
//verifica a existencia de registros e muda 
		//para o layout de visualiza��o
		if(carregaRegistros()) {
			setContentView(R.layout.mostralista);
			inicializaObjetos();
			carregaListeners();
			mostraRegistro();
		} else {
			mostraCxTexto("Nenhum registro cadastrado",
					"Aviso");
			iniciaAplicacao();
		}
	}

	void inicializaObjetos() {
//inicializa os objetos das views utilizando o
		//try/catch para evitar force-close da
		//aplica��o

//objetos do layout principal
		try{
			btnCadastro = (Button)
					findViewById(R.id.btnCadastro);
			btnMostra = (Button)
					findViewById(R.id.btnMostrar);
		} catch (Exception e){
			// N�o faz nada
		}

//objetos do layout de cadastramento
		try{
			campoNome = (EditText)
					findViewById(R.id.cad_nome);
			campoEndereco = (EditText)
					findViewById(R.id.cad_endereco);
			campoTelefone = (EditText)
					findViewById(R.id.cad_telefone);
			btnCadastrar = (Button)
					findViewById(R.id.cad_btnCadastrar);
			btnSair = (Button)
					findViewById(R.id.cad_btnSair);
		} catch (Exception e){
			// N�o faz nada
		}

//objetos do layout de visualiza��o de 
		//registros
		try{
			mostraNome =
					(TextView)findViewById(R.id.mostra_nome);
			mostraTelefone =
					(TextView)findViewById(R.id.mostra_telefone);
			mostraEndereco =
					(TextView)findViewById(R.id.mostra_endereco);
			btnAnterior = (Button)
					findViewById(R.id.mostra_btnAnterior);
			btnProximo = (Button)
					findViewById(R.id.mostra_btnProximo);
			btnVoltar = (Button)
					findViewById(R.id.mostra_btnVoltar);
		} catch (Exception e){
			// N�o faz nada
		}
	}

	void carregaListeners() {
//inicializa os listeners dos objetos 
		//utilizando o try/catch
//para evitar force-close da aplica��o

//listeners do layout principal
		try{
			btnCadastro.setOnClickListener(arg0 -> {
				abreBanco();
				iniciaCadastro();
			});
			btnMostra.setOnClickListener(arg0 -> {
				abreBanco();
				iniciaMostrarLista();
			});
		} catch (Exception e){
			// N�o faz nada
		}

//listeners do layout de cadastramento
		try{
			btnCadastrar.setOnClickListener(arg0 -> {
				try {
					insereRegistro();
					mostraCxTexto("Cadastro efetuado com sucesso", "Aviso");
					campoEndereco.setText(null);
					campoTelefone.setText(null);
					campoNome.setText(null);
					campoNome.requestFocus();
				} catch (Exception e) {
					mostraCxTexto("Erro ao cadastrar",
							"Erro");
				}
			});
			btnSair.setOnClickListener(arg0 -> iniciaAplicacao());
		} catch (Exception e){
			// N�o faz nada
		}

//listeners do layout de visualiza��o de 
		//registros
		try{
			btnVoltar.setOnClickListener(arg0 -> {
				c.close();
				iniciaAplicacao();
			});

			btnAnterior.setOnClickListener(arg0 -> mostraRegAnterior());

			btnProximo.setOnClickListener(arg0 -> mostraRegProximo());
		} catch (Exception e){
			// N�o faz nada
		}

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		abreBanco();
		iniciaAplicacao();
	}

}