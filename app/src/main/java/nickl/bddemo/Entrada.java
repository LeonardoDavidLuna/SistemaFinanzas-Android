    package nickl.bddemo;

import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Entrada extends Activity
{
    public final static String NUMERO="";
    private EditText et2, et3, et4;
    long ahora = System.currentTimeMillis();
    Date fecha2 = new Date(ahora);
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    String salida = df.format(fecha2);

    //Obtenemos el mes actual para buscarlo después
    String subFecha = salida.substring(3,5);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        et2 = (EditText) findViewById(R.id.editFecha);
        et3 = (EditText) findViewById(R.id.editCompra);
        et4 = (EditText) findViewById(R.id.editCosto);

        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        long ahora = System.currentTimeMillis();
        Date fecha2 = new Date(ahora);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String salida = df.format(fecha2);

        //Obtenemos el mes actual para buscarlo después
        String subFecha = salida.substring(3,5);

        et2.setText(salida);
    }
    // Guardamos un nuevo Gasto
    public void alta(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();

        String fecha    = et2.getText().toString();
        String compra   = et3.getText().toString();
        String costo    = et4.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("fecha", fecha);
        registro.put("compra", compra);
        registro.put("costo", costo);

        if(et2.getText().toString().equals("")||et3.getText().toString().equals("")||et4.getText().toString().equals(""))
        {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
        else
        {
            bd.insert("gastos", null, registro);
            bd.close();
            // ponemos los campos a vacío para insertar el siguiente usuario
            et2.setText(salida);
            et3.setText("");
            et4.setText("");
            Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
        }
    }
    // Borrar todas las entradas hechas
    public void bajaTodo(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

            // aquí borro la base de datos del usuario por el dni
            int cant = bd.delete("gastos", null, null);
            bd.close();

            et2.setText(salida);
            et3.setText("");
            et4.setText("");
            Toast.makeText(this, "¡Todo fue eliminado!", Toast.LENGTH_SHORT).show();
    }
    //Borrar todas las entradas del Mes
    public void bajaMes(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        // Filtro la fecha para borrar
        int cant = bd.delete("gastos" , "fecha LIKE '__%"+subFecha+"%__'", null);
        bd.close();

        et2.setText(salida);
        et3.setText("");
        et4.setText("");
        Toast.makeText(this, "Mes Actual Borrado", Toast.LENGTH_SHORT).show();
    }
    //Regresar a la Activity del Usuario
    public void volver(View v)
    {
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Usuario.class);
        ven.putExtra(NUMERO, numero);
        startActivity(ven);
        this.finish();
    }
}