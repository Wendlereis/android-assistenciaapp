package br.com.weis.exernac;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.weis.exernac.model.ChamadoModel;

public class PesquisarActivity extends AppCompatActivity {
    EditText txtCodigoFuncionario;
    ListView lstChamados;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);

        setupUI();
    }

    private void setupUI(){
        txtCodigoFuncionario = (EditText) findViewById(R.id.txtCodigoFuncionario);
        lstChamados = (ListView) findViewById(R.id.lstChamados);
    }

    public void pesquisar(View v){
        int codigoFuncionario = Integer.parseInt(txtCodigoFuncionario.getText().toString());

        PesquisaTask pesquisarTask = new PesquisaTask();
        pesquisarTask.execute(codigoFuncionario);
    }

    private class PesquisaTask extends AsyncTask<Integer, Void, String>{
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PesquisarActivity.this, "Aguarde", "Pesquisando Chamados");
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                URL url = new URL("https://assistenciaapi.herokuapp.com/rest/chamado/funcionario/" + params[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");

                StringBuilder resposta = new StringBuilder();

                if (urlConnection.getResponseCode() == 200){
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    String linha;

                    while ((linha = buffer.readLine()) != null){
                        resposta.append(linha);
                    }
                }

                urlConnection.disconnect();

                return resposta.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if (s != null){
                try {
                    JSONArray arrayJson = new JSONArray(s);

                    List<ChamadoModel> listaChamados = new ArrayList<ChamadoModel>();

                    for (int i = 0; i < arrayJson.length(); i++){
                        JSONObject item = (JSONObject) arrayJson.get(i);

                        int codigo = item.getInt("codigo");
                        int codigoFuncionario = item.getInt("codigoFuncionario");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date data = dateFormat.parse(item.getString("data"));

                        boolean finalizado = item.getBoolean("finalizado");
                        String descricao = item.getString("descricao");

                        listaChamados.add(new ChamadoModel(codigo, codigoFuncionario, data, finalizado, descricao));
                    }

                    ListAdapter adapter = new ArrayAdapter<ChamadoModel>(PesquisarActivity.this, android.R.layout.simple_list_item_1, listaChamados);

                    lstChamados.setAdapter(adapter);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(PesquisarActivity.this, "Funcionario nao encontrado", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(s);
        }
    }
}
