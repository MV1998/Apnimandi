package com.mohit.varma.apnimandi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mohit.varma.apnimandi.model.Item;
import com.mohit.varma.apnimandi.utilites.DataBaseInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDataBaseConnect extends SQLiteOpenHelper {

    private static final String KEY_ID = "id";
    //private static final String KEY_NAME = "name";

    /**
     * this are all column names
     * id
     * cut_off_price
     * image
     * name
     * price
     */

    private static final String ITEM_CUTOFF_PRICE = "cut_of_price";

    private static final String  ITEM_IMAGE = "image";

    private static final String ITEM_NAME = "item_name";

    private static final String ITEM_PRICE = "item_price";

    private static final String ITEM_ACTUAL_PRICE = "item_actual_price";

    private static final String INCREMENT_DECREMENT_VALUE = "increment_decrement_value";

    private static final String FINAL_PRICE = "final_price";

    private List<Item> itemList;
    private Context context;



    public SQLiteDataBaseConnect(Context context) {

        super(context, DataBaseInfo.DATABASE_NAME,null, DataBaseInfo.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

//
//        String createTable= "CREATE TABLE " + DataBaseInfo.TABLE_NAME+"("
//                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT"
//                + ")";

        String createanotherTable = "CREATE TABLE " + DataBaseInfo.TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEM_ACTUAL_PRICE +" INTEGER, "+
                INCREMENT_DECREMENT_VALUE +" INTEGER, " +
                FINAL_PRICE +" INTEGER, "+
                ITEM_CUTOFF_PRICE + " TEXT, " +
                ITEM_IMAGE + " BLOB, " +
                ITEM_NAME + " TEXT, " +
                ITEM_PRICE+ " TEXT" + ")";

        sqLiteDatabase.execSQL(createanotherTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DataBaseInfo.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }


    public boolean  AdditemtoDatabase(Item item){
        boolean dataadded = false;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ACTUAL_PRICE,item.getActual_price());
        contentValues.put(INCREMENT_DECREMENT_VALUE,item.getIncre_decre_price());
        contentValues.put(FINAL_PRICE,item.getFinal_price());
        contentValues.put(ITEM_CUTOFF_PRICE,item.getPercentOff());
        contentValues.put(ITEM_IMAGE,convertImagetoByteArray(item.getImageResourceId()));
        contentValues.put(ITEM_NAME,item.getItemName());
        contentValues.put(ITEM_PRICE,item.getItemPrice());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        dataadded =  sqLiteDatabase.insert(DataBaseInfo.TABLE_NAME,null,contentValues) > 0;

        return dataadded;
    }



    public Cursor getAllItem(){

        String query = "SELECT * FROM "+ DataBaseInfo.TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(DataBaseInfo.TABLE_NAME, new String[]{KEY_ID,ITEM_CUTOFF_PRICE,ITEM_IMAGE, ITEM_NAME,ITEM_PRICE}, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        return  cursor;
    }



    public List<Item> getitemarraylist(){

        String selectQuery = "SELECT  * FROM " + DataBaseInfo.TABLE_NAME ;

        //List<Item> employee_list = new ArrayList<>();
        itemList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String[] field = {KEY_ID,ITEM_ACTUAL_PRICE,INCREMENT_DECREMENT_VALUE,FINAL_PRICE,ITEM_CUTOFF_PRICE,ITEM_IMAGE,ITEM_NAME,ITEM_PRICE};
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        Cursor c = sqLiteDatabase.query(DataBaseInfo.TABLE_NAME, field, null, null, null, null, null);

        int keyindex = cursor.getColumnIndex(KEY_ID);

        int actual = cursor.getColumnIndex(ITEM_ACTUAL_PRICE);
        int incre_decre = cursor.getColumnIndex(INCREMENT_DECREMENT_VALUE);
        int finalprice = cursor.getColumnIndex(FINAL_PRICE);

        int cutOffPrice = cursor.getColumnIndex(ITEM_CUTOFF_PRICE);
        int itemimage = cursor.getColumnIndex(ITEM_IMAGE);
        int itemname = cursor.getColumnIndex(ITEM_NAME);
        int itemprice = cursor.getColumnIndex(ITEM_PRICE);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String string = cursor.getString(keyindex);
            String cutoffprice = cursor.getString(cutOffPrice);
            byte[] pic = cursor.getBlob(itemimage);
            Bitmap bitmap = convertByteArrayToBitmap(pic);
            String finalname = cursor.getString(itemname);
            String itemfinalprice = cursor.getString(itemprice);
            int actualprice = cursor.getInt(actual);
            int incredecre = cursor.getInt(incre_decre);
            int final_price = cursor.getInt(finalprice);

            itemList.add(new Item(cutoffprice,finalname,itemfinalprice,bitmap,string,actualprice,incredecre,final_price));

        }

        return itemList;
    }

    public List<Integer> getAllFinalpriced(){

        List<Integer> integerList = new ArrayList<>();

        String query = "SELECT " + FINAL_PRICE +" FROM " + DataBaseInfo.TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String[] field = {FINAL_PRICE};
        Cursor cursor = sqLiteDatabase.query(DataBaseInfo.TABLE_NAME,field,null,null,null,null,null);

        int finalpricecolumnIndex = cursor.getColumnIndex(FINAL_PRICE);

        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            int IntegerPrice = cursor.getInt(finalpricecolumnIndex);
            integerList.add(IntegerPrice);
        }

        return integerList;
    }

    public int getTotalResesult(){
        int total =0;

        for(Integer oneInteger : getAllFinalpriced()){
            total = total+oneInteger;
        }
        return total;
    }


    public boolean deleteItemfromDatabase(String id){
        return this.getWritableDatabase().delete(DataBaseInfo.TABLE_NAME, KEY_ID + "=?", new String[]{id}) > 0;
    }


    public void deleteItem(String string) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DataBaseInfo.TABLE_NAME, KEY_ID + "=" +string,null);
        db.close();
    }
//
//    public void updaterow(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DataBaseInfo.TABLE_NAME + "'");
//    }

    public void deitem(String string){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DataBaseInfo.TABLE_NAME);
        db.close();
    }

    public byte[] convertImagetoByteArray(int image){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0,byteArrayOutputStream);
        return  byteArrayOutputStream.toByteArray();
    }

    public Bitmap convertByteArrayToBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public void updateincrease(String id,int increasedValue){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INCREMENT_DECREMENT_VALUE,increasedValue);
        sqLiteDatabase.update(DataBaseInfo.TABLE_NAME,contentValues,KEY_ID +"="+ id,null);
    }
    public void updateFinalPrice(String id,int finalpriced){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FINAL_PRICE,finalpriced);
        sqLiteDatabase.update(DataBaseInfo.TABLE_NAME,contentValues,KEY_ID +"="+ id,null);
    }

}

//
//db.execSQL("INSERT INTO MyFilms (Title, Director, Year) VALUES('"
//        + title + "','" + director + "'," + year + ");");
