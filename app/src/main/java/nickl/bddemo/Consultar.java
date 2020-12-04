package nickl.bddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Consultar extends AppCompatActivity
{
    private EditText fecha, rubro;
    private TextView Aviso;
    private ListView lvA;
    ArrayList<String> lista;
    ArrayAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);
        fecha = (EditText) findViewById(R.id.editFecha);
        rubro = (EditText) findViewById(R.id.editRubro);
        lvA = (ListView) findViewById(R.id.listaA);
        Aviso = (TextView) findViewById(R.id.eti_Aviso);
    }
    //Función para Consultar Todos los Gastos
    public void vertodo(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        lista = admin.llenar();
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        lvA.setAdapter(adaptador);
        Aviso.setText("Todas las Entradas");
    }
    //Función para filtrar los datos por Mes
    public void vermes(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        lista = admin.llenar4();
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        lvA.setAdapter(adaptador);
        Aviso.setText("Mes");
    }
    //Función para filtrar por Fecha
    public void verfecha(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);
        String consulta = fecha.getText().toString();

            if(fecha.getText().toString().equals(""))
            {
                Toast.makeText(this, "Escribe una fecha", Toast.LENGTH_SHORT).show();
            }
            else
                {
                lista = admin.llenar2(consulta);
                adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
                lvA.setAdapter(adaptador);

                String titulo = fecha.getText().toString();
                Aviso.setText("Fecha "+titulo);
            }
    }
    //Función para filtrar por Rubro
    public void verrubro(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Tabla2", null, 1);

        String consulta = rubro.getText().toString();

        if(rubro.getText().toString().equals(""))
        {
            Toast.makeText(this, "Escribe un rubro", Toast.LENGTH_SHORT).show();
        }else
        {
            lista = admin.llenar3(consulta);
            adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
            lvA.setAdapter(adaptador);

            String titulo = rubro.getText().toString();
            Aviso.setText("Rubro "+titulo);
        }
    }
}