package nickl.bddemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Configuracion extends AppCompatActivity
{
    public final static String NUMERO="";

    private EditText et2, et3, et4, et5;
    private TextView et1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        et1 = (TextView) findViewById(R.id.editNumero);
        et2 = (EditText) findViewById(R.id.editNombre);
        et3 = (EditText) findViewById(R.id.editSaldo);
        et4 = (EditText) findViewById(R.id.editCorreo);
        et5 = (EditText) findViewById(R.id.editContraseña);

        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        if(numero.equals(""))//Verificamos que no esté vacío el campo
        {
            Toast.makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }
        else{
            Cursor fila = bd.rawQuery("select numero, nombre, saldo, correo, contraseña from usuario where numero=" + numero, null);
            if (fila.moveToFirst())
            {
                et1.setText(fila.getString(0));
                et2.setText(fila.getString(1));
                et3.setText(fila.getString(2));
                et4.setText(fila.getString(3));
                et5.setText(fila.getString(4));
            } else
                Toast.makeText(this, "No existe usuario con el número: "+numero,Toast.LENGTH_SHORT).show();
            bd.close();
        }
    }
    // Método para dar de baja al usuario insertado
    public void baja(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String numero = et1.getText().toString();

        if(et1.getText().toString().equals(""))
        {
            Toast.makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }else
        {
            int cant = bd.delete("usuario", "numero=" + numero, null);
            bd.close();
            //Limpiamos los campos tras borrar
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");
            et5.setText("");
            if (cant == 1)
            {
                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                this.finish();
                Intent ven=new Intent(this,MainActivity.class);
                startActivity(ven);
            }
            else
                Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
        }
    }
    // Método para modificar la información del usuario
    public void modificacion(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();

        String numero     = et1.getText().toString();
        String nombre     = et2.getText().toString();
        String saldo      = et3.getText().toString();
        String correo     = et4.getText().toString();
        String contraseña = et5.getText().toString();

        ContentValues registro = new ContentValues();

        // actualizamos con los nuevos datos, la información cambiada
        registro.put("nombre",     nombre);
        registro.put("saldo",      saldo);
        registro.put("correo",     correo);
        registro.put("contraseña", contraseña);

        if(et1.getText().toString().equals(""))//Verificamos que no esté vacío
        {
            Toast.makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }else
        {
            int cant = bd.update("usuario", registro, "numero=" + numero, null);
            bd.close();
            if (cant == 1)
                Toast.makeText(this, "Modificado con éxito", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
        }
    }
    public void volver(View v)
    {
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Usuario.class);
        ven.putExtra(NUMERO, numero);
        startActivity(ven);
    }
}