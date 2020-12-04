package nickl.bddemo;

import android.content.Intent;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Registro extends Activity
{
    private EditText et1, et2, et3, et4, et5, et6;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        et1 = (EditText) findViewById(R.id.editNumero);
        et2 = (EditText) findViewById(R.id.editNombre);
        et3 = (EditText) findViewById(R.id.editSaldo);
        et4 = (EditText) findViewById(R.id.editCorreo);
        et5 = (EditText) findViewById(R.id.editContraseña);
        et6 = (EditText) findViewById(R.id.editConfirmaContraseña);
    }

    // Registrar Usuario Nuevo
    public void alta(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String numero     = et1.getText().toString();
        String nombre     = et2.getText().toString();
        String saldo      = et3.getText().toString();
        String correo     = et4.getText().toString();
        String contraseña = et5.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("numero", numero);
        registro.put("nombre", nombre);
        registro.put("saldo", saldo);
        registro.put("correo", correo);
        registro.put("contraseña", contraseña);

        if(et1.getText().toString().equals("")||et2.getText().toString().equals("")||et3.getText().toString().equals("")||et4.getText().toString().equals("")||et5.getText().toString().equals("")||et6.getText().toString().equals(""))
        {
            Toast.makeText(this, "Completa todos los campos",Toast.LENGTH_SHORT).show();
        }else
            if(et5.getText().toString().equals(et6.getText().toString()))
            {
                // los inserto en la base de datos
                bd.insert("usuario", null, registro);
                bd.close();

                // ponemos los campos a vacío para insertar el siguiente usuario
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                et6.setText("");
                Toast.makeText(this, "Registrado con éxito", Toast.LENGTH_SHORT).show();
                //Abrimos Activity Principal tras registrar.
                Intent ven=new Intent(this,MainActivity.class);
                startActivity(ven);
                this.finish();
            }
            else
            {
                Toast.makeText(this, "Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            }
    }
}