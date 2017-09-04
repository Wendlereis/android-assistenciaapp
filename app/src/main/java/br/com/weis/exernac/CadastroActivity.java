package br.com.weis.exernac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import br.com.weis.exernac.model.ChamadoModel;

public class CadastroActivity extends AppCompatActivity {
    private EditText txtCodigoFuncionario;
    private Spinner spTipoChamado;
    private ProgressDialog progress;
    private CheckBox chFinalizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        if (item.getItemId() == R.id.mnCadastro){
            intent = new Intent(this, CadastroActivity.class);
        }
        else {
            intent = new Intent(this, PesquisarActivity.class);
        }

        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    private void setupUI(){
        txtCodigoFuncionario = (EditText) findViewById(R.id.txtCodigoFuncionario);
        spTipoChamado = (Spinner) findViewById(R.id.snTipoChamado);
        chFinalizado = (CheckBox) findViewById(R.id.chFinalizado);
    }

    public void cadastrar(View v){
        int codigoFuncionario = Integer.parseInt(txtCodigoFuncionario.getText().toString());
        String tipoChamado = spTipoChamado.getSelectedItem().toString();
        Boolean finalizado = chFinalizado.isChecked();

        CadastroTask cadastroTask = new CadastroTask();
        cadastroTask.execute(new ChamadoModel(codigoFuncionario, finalizado, tipoChamado));
    }

    private class CadastroTask extends AsyncTask<ChamadoModel, Void, Integer>{
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(CadastroActivity.this, "Aguarde", "Cadastrando Im=nformações");
        }

        @Override
        protected Integer doInBackground(ChamadoModel... params) {
            try {
                URL url = new URL("https://assistenciaapi.herokuapp.com/rest/chamado");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JSONStringer jsonStringer = new JSONStringer();
                jsonStringer.object();
                jsonStringer.key("codigoFuncionario").value(params[0].getCodigoFuncionario());
                jsonStringer.key("descricao").value(params[0].getDescricao());
                jsonStringer.key("finalizado").value(params[0].isFinalizado());
                jsonStringer.endObject();

                OutputStreamWriter stream = new OutputStreamWriter(urlConnection.getOutputStream());

                stream.write(jsonStringer.toString());
                stream.close();

                int a = urlConnection.getResponseCode();

                return a;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            progress.dismiss();

            if (s == 201){
                Toast.makeText(CadastroActivity.this, "Cadastro Realizado", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(CadastroActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
