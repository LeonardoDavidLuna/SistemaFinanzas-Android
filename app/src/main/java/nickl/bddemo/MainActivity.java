package nickl.bddemo;

import android.content.Intent;
import android.view.WindowManager;
import android.widget.Toast;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity
{
    private EditText et1, et2;
    public final static String EXTRA_MESSAGE="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // proviene del layout, son los campos de texto
        et1 = (EditText) findViewById(R.id.editNumero);
        et2 = (EditText) findViewById(R.id.editContraseña);
    }

    //Función para loguearte
    public void entrar(View v)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        String numero       = et1.getText().toString();
        String contraseña   = et2.getText().toString();

        if(et1.getText().toString().equals("")||et2.getText().toString().equals(""))//Verificamos que no esté vacío el campo
        {
            Toast.makeText(this, "Escribe tu número y contraseña",Toast.LENGTH_SHORT).show();
        }
        else
        {//Hacemos la consulta seleccionando solo número y contraseña
            Cursor fila=bd.rawQuery("select numero, contraseña from usuario where numero='"+numero+"' and contraseña='"+contraseña+"'",null);

            if (fila.moveToFirst())
            {
                String num  = fila.getString(0);
                String pass = fila.getString(1);
                if (numero.equals(num)&&contraseña.equals(pass))
                {
                    Intent ven=new Intent(this,Usuario.class);
                    ven.putExtra(EXTRA_MESSAGE, num);
                    startActivity(ven);
                }
            } else
                Toast.makeText(this, "¡Número o Contraseña incorrectos!: ",Toast.LENGTH_SHORT).show();
            bd.close();
        }
    }

    //Ir al Registro de Usuario Nuevo
    public void registro(View v)
    {
        Intent ven=new Intent(this,Registro.class);
        startActivity(ven);
    }
}