package de.hska.trinkertinder30.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.hska.trinkertinder30.domain.Kontakt;
import de.hska.trinkertinder30.domain.Veranstaltung;

/**
 * Datenbankklasse damit Veranstaltungen, Benutzer und Kategorien abgespeichert und ausgelesen werden können.
 * Es werden drei Tabellen angelegt, entsprechend der Domain-Klassen
 *
 * @Version 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database.db";

    public static final String TABLE_NAME_KATEGORIE ="kategorien";
    public static final String COLUMN_ID = "id";

    public static final String TABLE_NAME_KONTAKTE = "kontakte";
    public static final String COLUMN_NAME = "nachnname";
    public static final String COLUMN_VORNAME = "vorname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORT = "passwort";

    public static final String TABLE_NAME_VERANSTALTUNG = "veranstaltung";
    public static final String COLUMN_BESCHREIBUNG = "beschreibung";
    public static final String COLUMN_KATEGORIE = "kategorie";
    public static final String COLUMN_DETAIL = "detail";
    public static final String COLUMN_VERANSTALTER = "veranstalter";
    public static final String COLUMN_TIMESTAMP = "sqltime";

    public static final String INSERT_SPORT = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Sport')";
    public static final String INSERT_ESSEN = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Essen')";
    public static final String INSERT_DATING = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Dating')";
    public static final String INSERT_KFZ = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('KFZ')";
    public static final String INSERT_NIGHTLIFE = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Nightlife')";
    public static final String INSERT_ELEKTRONIK = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Elektronik')";
    public static final String INSERT_NACHHILFE = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Nachhilfe')";
    public static final String INSERT_IMMOBILIEN = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Immobilien')";
    public static final String INSERT_JOBS = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Jobs')";
    public static final String INSERT_TICKETS = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Tickets & Eintrittkarten')";
    public static final String INSERT_VERSCHENKEN = "INSERT INTO " + TABLE_NAME_KATEGORIE + " (" + COLUMN_KATEGORIE + ") Values ('Zu Verschenken & Tauschen')";

    SQLiteDatabase db;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TABLE_CREATE_KATEGROIE = "CREATE TABLE "
            +TABLE_NAME_KATEGORIE
            +" ("
            +COLUMN_ID
            +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            + COLUMN_KATEGORIE
            +" TEXT NOT NULL);";

    private static final String TABLE_CREATE_KONTAKTE = "CREATE TABLE "
            + TABLE_NAME_KONTAKTE
            +" ("
            +COLUMN_ID
            +" INTEGER PRIMARY KEY NOT NULL , "
            +COLUMN_NAME
            +" TEXT NOT NULL , "
            +COLUMN_VORNAME
            +" TEXT NOT NULL , "
            +COLUMN_EMAIL
            +" TEXT NOT NULL , "
            + COLUMN_USERNAME
            +" TEXT NOT NULL , "
            + COLUMN_PASSWORT
            +" TEXT NOT NULL);";

    private static final String TABLE_CREATE_VERANSTALTUNG = "CREATE TABLE "
            + TABLE_NAME_VERANSTALTUNG
            +" ("
            +COLUMN_ID
            +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            +COLUMN_BESCHREIBUNG
            +" TEXT NOT NULL , "
            +COLUMN_DETAIL
            +" TEXT NOT NULL , "
            +COLUMN_VERANSTALTER
            +" TEXT NOT NULL , "
            + COLUMN_KATEGORIE
            +" TEXT NOT NULL , "
            + COLUMN_TIMESTAMP
            +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL);";

    /**
     * Schreibt in die Tabelle kontakte einen neuen Benutzer
     * @param kontakt der Kontakt der in die Datenbank geschrieben wird
     */
    public void insertContact(Kontakt kontakt){

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        String query = "SELECT * FROM " + TABLE_NAME_KONTAKTE;

        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME, kontakt.getName());
        values.put(COLUMN_VORNAME, kontakt.getVorname());
        values.put(COLUMN_EMAIL, kontakt.getEmail());
        values.put(COLUMN_USERNAME, kontakt.getUname());
        values.put(COLUMN_PASSWORT, kontakt.getPass());

        db.insert(TABLE_NAME_KONTAKTE, null, values);
        db.close();
    }

    /**
     * Überprüft ob das Passwort zu Benutzernamen übereinstimmt
     * @param username Name der mit passenden Passwort übereinstimmen soll
     * @return Passwort des Users
     */
    public String searchPassword(String username){

        db = this.getReadableDatabase();

        String query = "SELECT  " + COLUMN_USERNAME + ", " + COLUMN_PASSWORT + " FROM " + TABLE_NAME_KONTAKTE;

        Cursor cursor = db.rawQuery(query, null);

        String firststr, secondstr;
        secondstr = "not found";

        if(cursor.moveToFirst()){
            do{
                firststr = cursor.getString(0);

                if(firststr.equals(username)){

                    secondstr = cursor.getString(1);
                    break;
                }

            }
            while (cursor.moveToNext());
        }

        return secondstr;
    }

    /**
     * Holt alle Benutzerinformationen zu gegegebenen Benutzername
     * @param username Benutzernamen der zu den passenden Benutzerinformationen übereinstimmen soll
     * @return Alle Informationen über den Benutzer
     */
    public List<Kontakt> getContactToShow(String username){

        List<Kontakt> contacts = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_KONTAKTE + " WHERE "+ COLUMN_USERNAME + " ='" + username + "'", null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String vorname = cursor.getString(cursor.getColumnIndex(COLUMN_VORNAME));
                    String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                    String passwort = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORT));

                    contacts.add(new Kontakt(name, vorname, email,  passwort));

                } while (cursor.moveToNext());
            }
        }
        return contacts;
    }

    /**
     * Sammelt alle Kategorien die in der Datenbank abgespeichert sind
     * @return alle Kategorien
     */
    public ArrayList<String> getAllKategorien(){

        ArrayList<String> kategorien = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_KATEGORIE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                kategorien.add(cursor.getString(1));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return kategorien;
    }

    /**
     * Schreibt eine neue Veranstaltung in die Datenbank
     * @param veranstaltung Veranstaltung die in die Datenbank gespeichert werden sol
     */
    public void insertVeranstaltung(Veranstaltung veranstaltung){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "SELECT * FROM " + TABLE_NAME_VERANSTALTUNG;

        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_BESCHREIBUNG, veranstaltung.getBeschreibung());
        values.put(COLUMN_DETAIL, veranstaltung.getDetail());
        values.put(COLUMN_VERANSTALTER, veranstaltung.getVeranstalter());
        values.put(COLUMN_KATEGORIE, veranstaltung.getKategorie());

        db.insert(TABLE_NAME_VERANSTALTUNG, null, values);
        db.close();
    }

    /**
     * Sammelt alle Veranstaltungen der angeforderten Kategorie
     * @param kategoriename Kategorie die für alle Veranstaltungen
     * @return Veranstaltungen passend zu gegebenen Kategorienamen
     */
    public ArrayList<String> getAllBeschreibungen(String kategoriename){

        ArrayList<String> beschreibungen = new ArrayList<String>();

        String selectQuery = "SELECT "+ COLUMN_BESCHREIBUNG + ", " + COLUMN_KATEGORIE  +" FROM " + TABLE_NAME_VERANSTALTUNG + " WHERE " + COLUMN_KATEGORIE + " =  '" + kategoriename +"'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String beschreibung = cursor.getString(cursor.getColumnIndex(COLUMN_BESCHREIBUNG));
                    beschreibungen.add(beschreibung);

                } while (cursor.moveToNext());
            }
        }
        return beschreibungen;
    }

    /**
     * Sammelt die Informationen für die gegebene Veranstaltung mit passendem Benutzernamen
     * @param beschreibungstext Titel der Veranstaltung
     * @return Gibt Benutzernamen und Informationen zur passenden Veranstaltung zurück
     */
    public ArrayList<String> getDetailsAndUser(String beschreibungstext){

        ArrayList<String> detailsAndUser = new ArrayList<String>();

        String selectQuery = "SELECT "+ COLUMN_DETAIL + " , " + COLUMN_VERANSTALTER +" FROM " + TABLE_NAME_VERANSTALTUNG + " WHERE " + COLUMN_BESCHREIBUNG + " =  '" + beschreibungstext +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String detail = cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL));
                    String username = cursor.getString(cursor.getColumnIndex(COLUMN_VERANSTALTER));

                    detailsAndUser.add(detail);
                    detailsAndUser.add(username);
                } while (cursor.moveToNext());
            }
        }
        return detailsAndUser;
    }

    /**
     * Holt alle Veranstaltungen aus der Datenbank für die Anzeige in der Liste des Hauptmenüs, limitiert auf die 4 neuesten Veranstaltungen
     * @return die vier neuesten Veranstaltungen
     */
    public ArrayList<Veranstaltung> getAllVeranstaltungen(){

        ArrayList<Veranstaltung> detailsAndUser = new ArrayList<Veranstaltung>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME_VERANSTALTUNG + " ORDER BY " + COLUMN_ID + " DESC LIMIT 4  ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String beschreibung = cursor.getString(cursor.getColumnIndex(COLUMN_BESCHREIBUNG));
                    String kategorie = cursor.getString(cursor.getColumnIndex(COLUMN_KATEGORIE));
                    String username = cursor.getString(cursor.getColumnIndex(COLUMN_VERANSTALTER));
                    String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));

                    detailsAndUser.add(new Veranstaltung(beschreibung, "",username, kategorie, timestamp));
                } while (cursor.moveToNext());
            }
        }
        return detailsAndUser;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_VERANSTALTUNG);
        db.execSQL(TABLE_CREATE_KONTAKTE);
        db.execSQL(TABLE_CREATE_KATEGROIE);
        db.execSQL(INSERT_DATING);
        db.execSQL(INSERT_ESSEN);
        db.execSQL(INSERT_SPORT);
        db.execSQL(INSERT_KFZ);
        db.execSQL(INSERT_NIGHTLIFE);
        db.execSQL(INSERT_ELEKTRONIK);
        db.execSQL(INSERT_NACHHILFE);
        db.execSQL(INSERT_IMMOBILIEN);
        db.execSQL(INSERT_JOBS);
        db.execSQL(INSERT_TICKETS);
        db.execSQL(INSERT_VERSCHENKEN);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = "DROP TABLE IF EXISTS " + TABLE_NAME_VERANSTALTUNG;
        db.execSQL(query1);
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME_KONTAKTE;
        db.execSQL(query2);
        String query3 = "DROP TABLE IF EXISTS " + TABLE_NAME_KATEGORIE;
        db.execSQL(query3);
        this.onCreate(db);

    }
}
