package dks.attendance;

/**
 * Created by DKS on 2/24/2017.
 */


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DKS on 2/21/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String dbname = "data.db";
    public static final String table_name = "record";
    public static final String ID = "ID";
    public static final String date = "date";
    public static final String sub = "subject";
    public static final String value = "attenvalue";
    public static final String user = "faculty";

    public DbHelper(Context context) {
        super(context, dbname, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+table_name+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+date+" TEXT, "+user+" TEXT, "+sub+" INTEGER, "+value+" INTEGER )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insetdata(String date,int subject,int data,String faculty){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.date,date);
        contentValues.put(this.sub,subject);
        contentValues.put(this.value,data);
        contentValues.put(this.user,faculty);
        long result = db.insert(table_name,null,contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getdata(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res= db.rawQuery("select `"+sub+"` , `"+value+"` , `"+user+"` from `"+table_name +"` where `"+this.date+"` like '"+date+"' ",null);
        return res;
    }
    public void dropdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM `"+table_name+"`");
    }

}
