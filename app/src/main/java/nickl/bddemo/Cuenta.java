package nickl.bddemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Cuenta extends AppCompatActivity
{
    private TextView et1, et2, et3, et4, et5;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        et1 = (TextView) findViewById(R.id.etiNumero);
        et2 = (TextView) findViewById(R.id.etiNombre);
        et3 = (TextView) findViewById(R.id.etiSaldo);
        et4 = (TextView) findViewById(R.id.etiCorreo);
        et5 = (TextView) findViewById(R.id.etiContraseña);

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
                bd.close();

            } else
                Toast.makeText(this, "No existe usuario con el número: "+numero,Toast.LENGTH_SHORT).show();
            bd.close();
        }
     }
}