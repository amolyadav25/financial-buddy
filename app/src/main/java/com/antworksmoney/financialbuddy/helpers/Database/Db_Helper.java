package com.antworksmoney.financialbuddy.helpers.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.Entity.ProfileInfo;

import java.util.ArrayList;


public class Db_Helper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    private static final String LEAD_DETAILS = "LEAD_DETAILS";
    private static final String lead_id = "_id";
    private static final String lead_person_details = "lead_person_details";
    private static final String IMAGE_URL = "Image_URL";
    private static final String REDIRECT_URL = "Redirect_url";
    private static final String DATE_TIME = "Date_time";
    private static final String Is_Read = "Is_Read";

    private static final String CONTACTDETAILS = "CONTACT_DETAILS";
    private static final String CONTACTNUMBER = "CONTACT_NUMBER";
    private static final String CONTACTTOKEN = "CONTACT_TOKEN";

    private static final String DATABASE_NAME = "hms_app.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_1 = "CREATE TABLE " + LEAD_DETAILS + " (" +
            lead_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Is_Read + " VARCHAR, " +
            IMAGE_URL + " VARCHAR, "+
            REDIRECT_URL + " VARCHAR, " +
            DATE_TIME + " VARCHAR, " +
            lead_person_details + " VARCHAR )";

    private static final String SQL_CREATE_TABLE_3 = "CREATE TABLE " + CONTACTDETAILS + " (" + CONTACTNUMBER + " VARCHAR, " + CONTACTTOKEN + " VARCHAR )";


    public Db_Helper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_1);
        db.execSQL(SQL_CREATE_TABLE_3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading the database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + LEAD_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTDETAILS);
    }

    public void insertContactNumber(String contact_number, String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTNUMBER, contact_number);
        contentValues.put(CONTACTTOKEN,userName);
        db.insert(CONTACTDETAILS, null, contentValues);
    }

    public ArrayList<ProfileInfo> getSosContacts(){
        ArrayList<ProfileInfo> infos = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String querry = "Select * from " + CONTACTDETAILS;
        Cursor cursor=db.rawQuery(querry,null);
        while (cursor.moveToNext()) {
            infos.add(new ProfileInfo(cursor.getString(1),cursor.getString(0)));
        }
        cursor.close();
        return infos;
    }



    public void deleteContactsFromTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ CONTACTDETAILS);
    }

    public void insertNotificationData(String is_Read,
                                       String imageUrl,
                                       String redirectUrl,
                                       String dateTime,
                                       String notificationData) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Is_Read, is_Read);
        contentValues.put(IMAGE_URL,imageUrl);
        contentValues.put(REDIRECT_URL,redirectUrl);
        contentValues.put(DATE_TIME,dateTime);
        contentValues.put(lead_person_details,notificationData);
        db.insert(LEAD_DETAILS, null, contentValues);
    }

    public int getUnreadMessagegeCount(){
        int messageCount;
        SQLiteDatabase db = this.getWritableDatabase();
        String querry = "Select * from " + LEAD_DETAILS + " where " + Is_Read +" = '0'";
        Cursor cursor = db.rawQuery(querry, null);
        messageCount = cursor.getCount();
        cursor.close();
        return messageCount;
    }

    public ArrayList<LoanInfoEntity> getAllLeadData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String querry = String.format("Select * from %s ORDER by %s DESC ", LEAD_DETAILS, DATE_TIME);
        Cursor cursor = db.rawQuery(querry, null);
        ArrayList<LoanInfoEntity> loanEntityArrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            LoanInfoEntity entity = new LoanInfoEntity();
            entity.setLoanId(cursor.getString(0));
            entity.setAuditDone(cursor.getString(1));
            entity.setLoanImageUrl(cursor.getString(2));
            entity.setEmail(cursor.getString(3));
            entity.setDateOfIncorporation(cursor.getString(4));
            entity.setLoanName(cursor.getString(5));

            loanEntityArrayList.add(entity);
        }
        cursor.close();
        return loanEntityArrayList;
    }



    public void deleteContactFromDb(String mobileNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String querry = "DELETE FROM CONTACT_DETAILS WHERE CONTACT_NUMBER = '"+mobileNumber+"'";
        db.execSQL(querry);
    }

    public void updateLeadStatus(String caseNumber, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        String querry = "UPDATE "+LEAD_DETAILS+" SET "+Is_Read+" = "+status+" WHERE "+lead_id+" = '"+caseNumber+"'";
        db.execSQL(querry);
    }




}