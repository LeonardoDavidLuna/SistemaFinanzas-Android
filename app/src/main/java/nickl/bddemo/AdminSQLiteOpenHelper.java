package nickl.bddemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper
{
    public AdminSQLiteOpenHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, nombre, factory, version);
    }
    //Definimos las tablas a usar
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //aquí creamos la tabla de usuario (dni, nombre, ciudad, numero)
        db.execSQL("CREATE TABLE usuario(numero integer PRIMARY KEY, nombre text, saldo integer, correo text, contraseña text)");
        db.execSQL("CREATE TABLE gastos(id integer PRIMARY KEY AUTOINCREMENT NOT NULL, fecha text, compra text, costo integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int version1, int version2)
    {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS gastos");
        onCreate(db);
    }
    //Filtrar la ListView por Todo
    public ArrayList llenar()
    {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor registros = database.rawQuery("SELECT * FROM gastos",null);
        if(registros.moveToFirst())
        {
            do{
                lista.add(registros.getString(1));
                lista.add(registros.getString(2));
                lista.add(registros.getString(3));
            }while(registros.moveToNext());
        }
        return lista;
    }
    //Consultar por Mes
    public ArrayList llenar4()
    {
        //Fecha para consultar
        long ahora = System.currentTimeMillis();
        Date fecha2 = new Date(ahora);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String salida = df.format(fecha2);

        //Obtenemos el mes actual para buscarlo después
        String subFecha = salida.substring(3,5);

        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor registros = database.rawQuery("SELECT * FROM gastos WHERE fecha LIKE '__%"+subFecha+"%__'", null);
        if(registros.moveToFirst())
        {
            do{
                lista.add(registros.getString(1));
                lista.add(registros.getString(2));
                lista.add(registros.getString(3));
            }while(registros.moveToNext());
        }
        return lista;
    }
    //Filtrar la ListView por Fecha
    public ArrayList llenar2(String consulta)
    {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor registros = database.rawQuery("SELECT * FROM gastos WHERE fecha='"+consulta+"'",null);
        if(registros.moveToFirst())
        {
            do{
                //lista.add(registros.getString(0));
                lista.add(registros.getString(1));
                lista.add(registros.getString(2));
                lista.add(registros.getString(3));
            }while(registros.moveToNext());
        }
        return lista;
    }
    //Filtrar la ListView por Compra
    public ArrayList llenar3(String consulta)
    {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor registros = database.rawQuery("SELECT * FROM gastos WHERE compra='"+consulta+"'",null);
        if(registros.moveToFirst())
        {
            do{
                //lista.add(registros.getString(0));
                lista.add(registros.getString(1));
                lista.add(registros.getString(2));
                lista.add(registros.getString(3));
            }while(registros.moveToNext());
        }
        return lista;
    }
}