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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario extends AppCompatActivity
{
    public final static String NUMERO="";
    private TextView Nombre, Saldo, Gasto, Alerta, Fecha;
    private EditText IngresoEventual, IngresoPeriodico;
    int sumatoria=0, numtotal;
    int numEventual, numPeriodico;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        Fecha = (TextView) findViewById(R.id.eti_Fecha);
        Nombre = (TextView) findViewById(R.id.edit_usuario);
        Saldo = (TextView) findViewById(R.id.Saldo);
        Gasto = (TextView) findViewById(R.id.Gasto);
        Alerta = (TextView) findViewById(R.id.eti_Alerta);
        IngresoEventual = (EditText) findViewById(R.id.editIngresoEventual);
        IngresoPeriodico = (EditText) findViewById(R.id.editIngresoPeriodico);

        //Obtenemos Fecha Actual
        Date d =new Date();
        SimpleDateFormat fecha=new SimpleDateFormat("d, MMMM 'del' yyyy");
        String fechacComplString = fecha.format(d);
        Fecha.setText(fechacComplString);

        //Fecha para consultar Saldo Total
        long ahora = System.currentTimeMillis();
        Date fecha2 = new Date(ahora);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String salida = df.format(fecha2);

        //Obtenemos el mes actual para buscarlo después
        String subFecha = salida.substring(3,5);

        //Obtenemos el Número del Usuario de la Main Activity para búsquedas posteriores
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Consultar Nombre y Saldo del Usuario
        //////////////////////////////////
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        if(numero.equals(""))//Verificamos que no esté vacío el campo
        {
            Toast.makeText(this, "Escribe un número primero",Toast.LENGTH_SHORT).show();
        }
        else
            {
            Cursor fila = bd.rawQuery("select nombre, saldo from usuario where numero=" + numero, null);

            if (fila.moveToFirst())
            {
                //Poner el Nombre de Usuario en Pantalla
                Nombre.setText(fila.getString(0));
                Saldo.setText(fila.getString(1));
                //Convertimos el Saldo total a Entero
                String total  = fila.getString(1);
                numtotal = Integer.parseInt(total);
            } else
                Toast.makeText(this, "No existe usuario con el número: "+numero,Toast.LENGTH_SHORT).show();
            bd.close();
        }
        /////////////////////////////////

        //Consultar gastos totales
        ////////////////////////////////
        AdminSQLiteOpenHelper admin2 = new AdminSQLiteOpenHelper(this,"Tabla2", null, 1);
        SQLiteDatabase bd2 = admin2.getReadableDatabase();

        if(numero.equals(""))//Verificamos que no esté vacío el campo
        {
            Toast.makeText(this, "Ingresa una entrada nueva",Toast.LENGTH_SHORT).show();
        }
        else{
            //Cursor fila = bd2.rawQuery("SELECT costo FROM gastos", null);
            //Consultar el gasto del mes actual
            Cursor fila = bd2.rawQuery("SELECT costo FROM gastos WHERE fecha LIKE '__%"+subFecha+"%__'", null);
            if (fila.moveToFirst())
            {
                do{
                    //Poner el Gasto del usuario
                    String costo  = fila.getString(0);
                    int numcosto = Integer.parseInt(costo);
                    sumatoria+=numcosto;//
                    String resultado= Integer.toString(sumatoria); //Acumulamos los gastos del mes
                    Gasto.setText(resultado);//
                }while(fila.moveToNext());
            } else
                Toast.makeText(this, "Ingresa una nueva entrada: ",Toast.LENGTH_SHORT).show();
            bd.close();
        }
        ////////////////////////////////

        //Enviar mensaje sobre el estado del saldo
        if(sumatoria<(numtotal-100))
            Alerta.setText("");
        else
            if(sumatoria>numtotal)
                Alerta.setText("¡Saldo Rebasado!");
            else
                Alerta.setText("¡Se termina tu saldo!");
    }
    // Método para añadir ingresos
    public void sumar(View v)
    {
        //Obtengo lo ingresado por el usuario en cadena
        String periodico = IngresoPeriodico.getText().toString();
        String saldo = IngresoEventual.getText().toString();
        String saldoFinal =Saldo.getText().toString();

        //Convierto lo ingresado a número
        numEventual = Integer.parseInt(periodico);
        numPeriodico = Integer.parseInt(saldo);
        //Convertimos el saldo del usuario a Entero
        int saldoFinalNum = Integer.parseInt(saldoFinal);

        //Sumo las cantidades
        int añadicion =numEventual+numPeriodico+saldoFinalNum;

        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        ContentValues registro = new ContentValues();

        // actualizamos con los nuevos datos, la información cambiada
        registro.put("saldo", añadicion);

        if(IngresoPeriodico.getText().toString().equals("")||IngresoEventual.getText().toString().equals(""))
        {
            Toast.makeText(this, "Ingresa una cantidad",Toast.LENGTH_SHORT).show();
        }
        else
        {
            int cant = bd.update("usuario", registro, "numero=" + numero, null);
            bd.close();
            if (cant == 1)
                Toast.makeText(this, "Sumado", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "No se pudo sumar", Toast.LENGTH_SHORT).show();
        }
        Saldo.setText(""+añadicion);

        if(sumatoria<(añadicion-100))
            Alerta.setText("");
        else
        if(sumatoria>numtotal)
            Alerta.setText("¡Saldo Rebasado!");
        else
            Alerta.setText("¡Se termina tu saldo!");
    }
    //Ir a la Acivity Cuenta
    public void cuenta(View v)
    {
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Cuenta.class);

        ven.putExtra(NUMERO, numero);
        startActivity(ven);
    }
    //Ir a la Activity Configuración
    public void configuracion(View v)
    {
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Configuracion.class);
        ven.putExtra(NUMERO, numero);
        startActivity(ven);
    }
    //Botón Salir
    public void salir(View v)
    {
        Intent ven=new Intent(this,MainActivity.class);
        startActivity(ven);
        this.finish();
    }
    //Ir a la Activity de Entrada de Gastos
    public void entrada(View v)
    {
        Intent intent = getIntent();
        String numero = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent ven=new Intent(this,Entrada.class);
        ven.putExtra(NUMERO, numero);
        startActivity(ven);
    }
    //Ir a la Activity de Consultar
    public void consultar(View v)
    {
        Intent ven=new Intent(this,Consultar.class);
        startActivity(ven);
    }
}