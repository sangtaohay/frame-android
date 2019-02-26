package com.sangtaohay.memestudio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sangtaohay.memestudio.contract.MemeContract;

public class MemeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_CREATE = "CREATE TABLE " + MemeContract.MemeEntry.TABLE + " ( " +
            MemeContract.MemeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MemeContract.MemeEntry.COL_CREATEDAT + " INTEGER NOT NULL, " +
            MemeContract.MemeEntry.COL_TEXT_TOP + " TEXT, " +
            MemeContract.MemeEntry.COL_TEXT_BOTTOM + " TEXT, " +
            MemeContract.MemeEntry.COL_IMAGE + " TEXT, " +
            MemeContract.MemeEntry.COL_BACKGROUNDCOLOR + " INTEGER, " +
            MemeContract.MemeEntry.COL_STATUS + " INTEGER, " +
            MemeContract.MemeEntry.COL_POSITION + " INTEGER, " +
            MemeContract.MemeEntry.COL_TITLE + " TEXT);";

    private static final String DATABASE_INIT = "INSERT INTO " + MemeContract.MemeEntry.TABLE + "(" +  MemeContract.MemeEntry.COL_TEXT_TOP + "," +  MemeContract.MemeEntry.COL_TEXT_BOTTOM + "," + MemeContract.MemeEntry.COL_CREATEDAT + "," + MemeContract.MemeEntry.COL_IMAGE +","+ MemeContract.MemeEntry.COL_STATUS+") VALUES (?,?, ?,?, ?);" ;

    private static final String DATABASE_ALTER_1_2_A = "ALTER TABLE " + MemeContract.MemeEntry.TABLE + " ADD COLUMN " + MemeContract.MemeEntry.COL_WIDTH + " INTEGER DEFAULT 0;";
    private static final String DATABASE_ALTER_1_2_B = "ALTER TABLE " + MemeContract.MemeEntry.TABLE + " ADD COLUMN " + MemeContract.MemeEntry.COL_HEIGHT + " INTEGER DEFAULT 0;";
    private static final String DATABASE_UPDATE_1_2 =  "UPDATE " + MemeContract.MemeEntry.TABLE + " SET "+ MemeContract.MemeEntry.COL_WIDTH +" = ?, "+ MemeContract.MemeEntry.COL_HEIGHT+" = ? WHERE "+ MemeContract.MemeEntry._ID +" = ?;";

    private static final String DATABASE_ALTER_2_3_A = "ALTER TABLE " + MemeContract.MemeEntry.TABLE + " ADD COLUMN " + MemeContract.MemeEntry.COL_TEXT_BOTTOM_LEFT + " TEXT DEFAULT 'YES';";
    private static final String DATABASE_ALTER_2_3_B = "ALTER TABLE " + MemeContract.MemeEntry.TABLE + " ADD COLUMN " + MemeContract.MemeEntry.COL_TEXT_BOTTOM_RIGHT + " TEXT DEFAULT 'NO';";

    private static final String DATABASE_INSERT_3_4 = "INSERT INTO " + MemeContract.MemeEntry.TABLE + "(" +  MemeContract.MemeEntry.COL_TEXT_TOP + "," +  MemeContract.MemeEntry.COL_TEXT_BOTTOM + "," + MemeContract.MemeEntry.COL_CREATEDAT + "," + MemeContract.MemeEntry.COL_IMAGE +","+ MemeContract.MemeEntry.COL_STATUS+","+ MemeContract.MemeEntry.COL_WIDTH+","+ MemeContract.MemeEntry.COL_HEIGHT+") VALUES (?,?,?,?,?,?,?);" ;

    private static final String DATABASE_ALTER_4_5 = "ALTER TABLE " + MemeContract.MemeEntry.TABLE + " ADD COLUMN " + MemeContract.MemeEntry.COL_TYPE + " INTEGER DEFAULT 1;";
    private static final String DATABASE_UPDATE_4_5 =  "UPDATE " + MemeContract.MemeEntry.TABLE + " SET "+ MemeContract.MemeEntry.COL_TYPE +" = 1 WHERE 1=1;";
    private static final String DATABASE_INSERT_4_5 = "INSERT INTO " + MemeContract.MemeEntry.TABLE + "(" +  MemeContract.MemeEntry.COL_TEXT_TOP + "," +  MemeContract.MemeEntry.COL_TEXT_BOTTOM + "," + MemeContract.MemeEntry.COL_CREATEDAT + "," + MemeContract.MemeEntry.COL_IMAGE +","+ MemeContract.MemeEntry.COL_STATUS+","+ MemeContract.MemeEntry.COL_WIDTH+","+ MemeContract.MemeEntry.COL_HEIGHT+","+ MemeContract.MemeEntry.COL_TYPE+") VALUES (?,?,?,?,?,?,?,?);" ;

    private static final String DATABASE_UPDATE_6_7 =  "UPDATE " + MemeContract.MemeEntry.TABLE + " SET "+ MemeContract.MemeEntry.COL_WIDTH +" = 480, "+ MemeContract.MemeEntry.COL_IMAGE + " = 'https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme%2F0029.jpg?alt=media&token=469627e8-92e6-4f0c-be3b-94eeb3044c75', "+ MemeContract.MemeEntry.COL_HEIGHT+" = 560 WHERE "+ MemeContract.MemeEntry.COL_IMAGE +" = 'https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0029.jpg?alt=media&token=f96a390a-0918-4da0-9e8c-c2a9f9d138a9';";

    private static final String DATABASE_ALTER_7_8_A= "ALTER TABLE " + MemeContract.MemeEntry.TABLE + " ADD COLUMN " + MemeContract.MemeEntry.COL_CODE + " TEXT;";
    private static final String DATABASE_UPDATE_7_8_A =  "UPDATE " + MemeContract.MemeEntry.TABLE + " SET "+ MemeContract.MemeEntry.COL_CODE +" = ? WHERE "+ MemeContract.MemeEntry.COL_IMAGE +" = ?;";

    private static final String DATABASE_ALTER_7_8_B= "ALTER TABLE " + MemeContract.MemeEntry.TABLE + " ADD COLUMN " + MemeContract.MemeEntry.COL_THUMB + " TEXT;";
    private static final String DATABASE_UPDATE_7_8_B =  "UPDATE " + MemeContract.MemeEntry.TABLE + " SET "+ MemeContract.MemeEntry.COL_THUMB +" = ? WHERE "+ MemeContract.MemeEntry.COL_CODE +" = ?;";

    private static final String DATABASE_UPDATE_8_9 =  "UPDATE " + MemeContract.MemeEntry.TABLE + " SET "+ MemeContract.MemeEntry.COL_THUMB +" = (SELECT " + MemeContract.MemeEntry.COL_IMAGE +" FROM " + MemeContract.MemeEntry.TABLE  + " WHERE "+ MemeContract.MemeEntry.COL_CODE +" = ? ) "+" WHERE "+ MemeContract.MemeEntry.COL_CODE +" = ?;";

    public MemeDbHelper(Context context) {
        super(context, MemeContract.DB_NAME, null, MemeContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        version1(db);
        version2(db);
        version3(db);
        version4(db);
        version5(db);
        version6(db);
        version7(db);
        version8(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            version2(db);
        }
        if (oldVersion < 3) {
            version3(db);
        }
        if (oldVersion < 4) {
            version4(db);
        }
        if (oldVersion < 5) {
            version5(db);
        }
        if (oldVersion < 6) {
            version6(db);
        }
        if (oldVersion < 7) {
            version7(db);
        }
        if (oldVersion < 8) {
            version8(db);
        }
        if (oldVersion < 9) {
            version9(db);
        }
    }
    private void version1(SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_INIT, new Object[]{"I DON'T KNOW", "WHAT IS MEME",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0001.jpg?alt=media&token=ae7ef726-d34b-4f27-b989-0a4361c590df",1});
        db.execSQL(DATABASE_INIT, new Object[]{"... AND TAKE MY MONEY", "OK?",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0002.png?alt=media&token=02cc09ff-7c03-41c2-8bbf-bda7494025d8",1});
        db.execSQL(DATABASE_INIT, new Object[]{"BECAUSE I'M THE BEST", "THEY SAID I'LL WIN",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0003.jpg?alt=media&token=efd463b9-b9a2-4d22-a62b-54732b08d73b",1});
        db.execSQL(DATABASE_INIT, new Object[]{"DO YOU KNOW WHY", "I'M KEEP ASKING STUPID QUESTION",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0004.jpg?alt=media&token=2677320e-8da6-40ee-ac08-987ba34de53b",1});
        db.execSQL(DATABASE_INIT, new Object[]{"ACTUALLY...", "I DON'T UNDERSTAND AT ALL",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0005.jpg?alt=media&token=1b196eeb-bfb2-4fdf-9284-acdd322730d8",1});
        db.execSQL(DATABASE_INIT, new Object[]{ "ARE YOU KIDDING ME?", "ABOUT THAT?",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0006.jpg?alt=media&token=a52b033b-8119-4b8f-ab27-a84b1e8edec0",1});
        db.execSQL(DATABASE_INIT, new Object[]{"DON'T LIE ME", "I'VE ALREADY KNOWN THE TRUTH",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0007.jpg?alt=media&token=56c398a8-b4e9-48fc-afe9-5c9121eedbf8",1});
        db.execSQL(DATABASE_INIT, new Object[]{"THE REASON IS", "BECAUSE OF YOU",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0008.jpg?alt=media&token=c9b23b34-9493-4382-9ced-cbe915e5dbe0",1});
        db.execSQL(DATABASE_INIT, new Object[]{"I'M THE FIRST ONE", "WHO FINISH THE EXAM",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0009.jpg?alt=media&token=ea4883db-3d6a-4a7e-9b98-a083831ce4e4",1});
        db.execSQL(DATABASE_INIT, new Object[]{"NOT INTERESTED", "DON'T TELL ME",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0010.jpg?alt=media&token=204d6f74-a7b4-473f-9635-974e848548e9",1});
        db.execSQL(DATABASE_INIT, new Object[]{"DID YOU MEAN", "OHH! IMPOSSIBLE",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0011.jpg?alt=media&token=bc13f2e4-ba07-4a75-9c77-16f19f8f5da2",1});
        db.execSQL(DATABASE_INIT, new Object[]{"SHE SAID BECAUSE", "I'M A ...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0012.jpg?alt=media&token=d2f81d58-56a6-4dbc-b912-a2c2c5911837",1});
        db.execSQL(DATABASE_INIT, new Object[]{"YOU ARE RIGHT", "BUT ...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0013.jpg?alt=media&token=4a03a0f2-5d7c-4a67-891e-39873ded5e6b",1});
        db.execSQL(DATABASE_INIT, new Object[]{ "YOU DON'T SAY", "HOW CAN I KNOW?",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0014.jpg?alt=media&token=38d583e9-3920-45f2-85df-a8e58e56d0d5",1});
        db.execSQL(DATABASE_INIT, new Object[]{"WATCHING ROMANTIC", "....ALONE",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0015.jpg?alt=media&token=4b105143-f0e3-49f6-9e06-85945a1048fd",1});
        db.execSQL(DATABASE_INIT, new Object[]{"I'M A BIG FAN", "OF THIS APP",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0016.jpg?alt=media&token=4b20b2af-1070-43ad-97d0-0b84c82e525e",1});
        db.execSQL(DATABASE_INIT, new Object[]{"BUT HE TOLD ME", "YOU ARE THE BEST",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0017.jpg?alt=media&token=8c18d105-2030-495a-b844-8498a9c828ff",1});
        db.execSQL(DATABASE_INIT, new Object[]{"YOU GOT IT!", "AFTER SOME TIMES",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0018.png?alt=media&token=a7229fe0-68a5-41e5-b7d7-f22ef49ab40f",1});
    }
    private void version2(SQLiteDatabase db){
        db.execSQL(DATABASE_ALTER_1_2_A);
        db.execSQL(DATABASE_ALTER_1_2_B);
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 468, 1});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 360, 2});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 755, 3});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 485, 4});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 360, 5});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 444, 6});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 480, 7});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 449, 8});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 425, 9});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 480, 10});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 617, 11});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 387, 12});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 448, 13});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 598, 14});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 481, 15});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 427, 16});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 448, 17});
        db.execSQL(DATABASE_UPDATE_1_2, new Object[]{640, 427, 18});
    }
    private void version3(SQLiteDatabase db){
        db.execSQL(DATABASE_ALTER_2_3_A);
        db.execSQL(DATABASE_ALTER_2_3_B);
    }
    private void version4(SQLiteDatabase db){
        db.execSQL(DATABASE_INSERT_3_4, new Object[]{"I'M RARELY ASK PEOPLE", "BUT WHEN I DO, ...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0019.jpg?alt=media&token=7aaacd04-f034-4a3f-802a-ae24fffaf2e1",1, 640, 812});
        db.execSQL(DATABASE_INSERT_3_4, new Object[]{"DO YOU KNOW", "WHAT I MEAN",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0020.jpg?alt=media&token=a61985bd-eb6f-4558-b499-9054b2142630",1, 640, 620});
        db.execSQL(DATABASE_INSERT_3_4, new Object[]{"THIS IS MY FACE", "WHEN...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0021.jpg?alt=media&token=8e417ea9-ade5-4339-b80f-36e615af2744",1, 640, 361});
        db.execSQL(DATABASE_INSERT_3_4, new Object[]{"OH PLEASE", "DON'T TELL ME MORE",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0022.jpg?alt=media&token=039a92e2-a599-494e-bf06-04827ca51e1a",1, 640, 593});
        db.execSQL(DATABASE_INSERT_3_4, new Object[]{"WHAT IF I TOLD YOU", "...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0023.jpg?alt=media&token=3112d71e-d530-494b-9f3a-a42941b56177",1, 640, 388});
        db.execSQL(DATABASE_INSERT_3_4, new Object[]{"I DON'T KNOW WHO YOU ARE", "BUT...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0024.png?alt=media&token=89923039-50af-4547-a376-87a996219a1a",1, 640, 426});
    }
    private void version5(SQLiteDatabase db){
        db.execSQL(DATABASE_ALTER_4_5);
        db.execSQL(DATABASE_UPDATE_4_5);
        db.execSQL(DATABASE_INSERT_4_5, new Object[]{"DO IT NOW!", "AND TAKE MY MONEY",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2002.gif?alt=media&token=b3ee3767-2bbd-422e-bffb-382bdd669ffc",1, 620, 360, 2});
        db.execSQL(DATABASE_INSERT_4_5, new Object[]{"ARE YOU SERIOUS?", "TAKE IT EASY, MAN",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2013.gif?alt=media&token=fd7f025a-fc21-4832-94b0-922cbec92570",1, 480, 270, 2});

    }
    private void version6(SQLiteDatabase db){
        db.execSQL(DATABASE_INSERT_4_5, new Object[]{"WHAT DO YOU MEAN?", "I DON'T BELIEVE",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0027.png?alt=media&token=81ce3056-61ea-4f93-adc2-d0cba9e01711",1, 465, 262, 1});
        db.execSQL(DATABASE_INSERT_4_5, new Object[]{"YOU MEAN TO TELL ME", "YOU DON'T KNOW HOW TO...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0029.jpg?alt=media&token=f96a390a-0918-4da0-9e8c-c2a9f9d138a9",1, 600, 828, 1});
        db.execSQL(DATABASE_INSERT_4_5, new Object[]{"YOU MEAN TO TELL ME", "YOU DON'T KNOW HOW TO...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0030.jpg?alt=media&token=36f8ca71-8117-4119-ab0e-a2c593cd85f9",1, 429, 338, 1});
        db.execSQL(DATABASE_INSERT_4_5, new Object[]{"...", "...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2025.gif?alt=media&token=e36c6da9-e1ec-47af-8108-58ec61cb9fef",1, 500, 350, 2});
        db.execSQL(DATABASE_INSERT_4_5, new Object[]{"...", "...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2026.gif?alt=media&token=6c833d7c-0a58-42b9-a791-962d4d933e34",1, 500, 273, 2});
        db.execSQL(DATABASE_INSERT_4_5, new Object[]{"...", "...",1,"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2028.gif?alt=media&token=3c41e081-1a34-417f-bd0b-b344c4b36593",1, 450, 288, 2});

    }
    private void version7(SQLiteDatabase db){
        db.execSQL(DATABASE_UPDATE_6_7);
    }
    private void version8(SQLiteDatabase db){
        db.execSQL(DATABASE_ALTER_7_8_A);
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0001", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0001.jpg?alt=media&token=ae7ef726-d34b-4f27-b989-0a4361c590df"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0002", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0002.png?alt=media&token=02cc09ff-7c03-41c2-8bbf-bda7494025d8"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0003", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0003.jpg?alt=media&token=efd463b9-b9a2-4d22-a62b-54732b08d73b"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0004", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0004.jpg?alt=media&token=2677320e-8da6-40ee-ac08-987ba34de53b"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0005", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0005.jpg?alt=media&token=1b196eeb-bfb2-4fdf-9284-acdd322730d8"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0006", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0006.jpg?alt=media&token=a52b033b-8119-4b8f-ab27-a84b1e8edec0"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0007", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0007.jpg?alt=media&token=56c398a8-b4e9-48fc-afe9-5c9121eedbf8"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0008", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0008.jpg?alt=media&token=c9b23b34-9493-4382-9ced-cbe915e5dbe0"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0009", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0009.jpg?alt=media&token=ea4883db-3d6a-4a7e-9b98-a083831ce4e4"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0010", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0010.jpg?alt=media&token=204d6f74-a7b4-473f-9635-974e848548e9"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0011", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0011.jpg?alt=media&token=bc13f2e4-ba07-4a75-9c77-16f19f8f5da2"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0012", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0012.jpg?alt=media&token=d2f81d58-56a6-4dbc-b912-a2c2c5911837"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0013", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0013.jpg?alt=media&token=4a03a0f2-5d7c-4a67-891e-39873ded5e6b"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0014", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0014.jpg?alt=media&token=38d583e9-3920-45f2-85df-a8e58e56d0d5"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0015", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0015.jpg?alt=media&token=4b105143-f0e3-49f6-9e06-85945a1048fd"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0016", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0016.jpg?alt=media&token=4b20b2af-1070-43ad-97d0-0b84c82e525e"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0017", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0017.jpg?alt=media&token=8c18d105-2030-495a-b844-8498a9c828ff"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0018", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0018.png?alt=media&token=a7229fe0-68a5-41e5-b7d7-f22ef49ab40f"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0019", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0019.jpg?alt=media&token=7aaacd04-f034-4a3f-802a-ae24fffaf2e1"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0020", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0020.jpg?alt=media&token=a61985bd-eb6f-4558-b499-9054b2142630"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0021", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0021.jpg?alt=media&token=8e417ea9-ade5-4339-b80f-36e615af2744"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0022", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0022.jpg?alt=media&token=039a92e2-a599-494e-bf06-04827ca51e1a"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0023", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0023.jpg?alt=media&token=3112d71e-d530-494b-9f3a-a42941b56177"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0024", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0024.png?alt=media&token=89923039-50af-4547-a376-87a996219a1a"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0027", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0027.png?alt=media&token=81ce3056-61ea-4f93-adc2-d0cba9e01711"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0029", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme%2F0029.jpg?alt=media&token=469627e8-92e6-4f0c-be3b-94eeb3044c75"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"0030", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F0030.jpg?alt=media&token=36f8ca71-8117-4119-ab0e-a2c593cd85f9"});

        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"2002", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2002.gif?alt=media&token=b3ee3767-2bbd-422e-bffb-382bdd669ffc"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"2013", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2013.gif?alt=media&token=fd7f025a-fc21-4832-94b0-922cbec92570"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"2025", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2025.gif?alt=media&token=e36c6da9-e1ec-47af-8108-58ec61cb9fef"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"2026", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2026.gif?alt=media&token=6c833d7c-0a58-42b9-a791-962d4d933e34"});
        db.execSQL(DATABASE_UPDATE_7_8_A, new Object[]{"2028", "https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/meme-studio%2F2028.gif?alt=media&token=3c41e081-1a34-417f-bd0b-b344c4b36593"});

        db.execSQL(DATABASE_ALTER_7_8_B);
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0001.jpg?alt=media&token=2557450e-aeb2-4d22-873b-00f153db740d","0001"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0002.png?alt=media&token=a600b778-a105-46a7-b73d-d1f3df890e71","0002"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0003.jpg?alt=media&token=433c0c0f-2cd2-44f8-b27e-ff05a56d9e38","0003"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0004.jpg?alt=media&token=0320fa68-0675-4357-9599-aa0f92aae81b","0004"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0005.jpg?alt=media&token=e503e985-5175-4803-aa5f-ec3f2fce1451","0005"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0006.jpg?alt=media&token=94eb4a9e-7fb3-4c48-884b-95bd648458da","0006"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0007.jpg?alt=media&token=c0b26412-3b57-4992-9753-c740986c8db4","0007"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0008.jpg?alt=media&token=4abf940b-5783-4ab5-976b-13363767393d","0008"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0009.jpg?alt=media&token=d6b6165c-eab8-4a3b-9eee-887eac57d136","0009"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0010.jpg?alt=media&token=09f1e9ee-a5fc-4999-af69-6c42590ef87b","0010"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0011.jpg?alt=media&token=3a51082c-52d7-43e8-9962-3ecdcc2d318c","0011"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0012.jpg?alt=media&token=4909f39f-7af4-4101-af5c-1d6aed2b70c6","0012"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0013.jpg?alt=media&token=2ec44aa4-769b-4430-8b27-cc14e716ade0","0013"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0014.jpg?alt=media&token=0d913a32-4b30-40aa-a106-08120441b1d8","0014"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0015.jpg?alt=media&token=1f179fb6-e222-42dc-91b4-8c737e4b2257","0015"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0016.jpg?alt=media&token=288de262-c685-48ef-b44c-dc0b36d79e2f","0016"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0017.jpg?alt=media&token=756f396c-1d53-4187-90a3-e77cb7ae1943","0017"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0018.png?alt=media&token=8deffcd6-a1f3-45cf-ab31-43b6e8360eda","0018"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0019.jpg?alt=media&token=a08b1491-3e25-407e-a252-8a2c2bd8db9c","0019"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0020.jpg?alt=media&token=8ccf10ea-8a22-4464-a01a-b3594cee2a30","0020"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0021.jpg?alt=media&token=593ed179-9096-437c-9a50-9eaa6742879e","0021"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0022.jpg?alt=media&token=64ad3cd3-aa4f-4d5e-bf58-377e0989d672","0022"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0023.jpg?alt=media&token=2c206b2f-afbe-45cd-8088-782ec43cca57","0023"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0024.png?alt=media&token=d65d4365-a788-4a98-bf1a-7b9569301391","0024"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0027.png?alt=media&token=8abb8cec-1b08-458a-b14b-990a7e72a359","0027"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0029.jpg?alt=media&token=45f3a435-7440-4d1d-a3fa-059f8a46dbd1","0029"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{"https://firebasestorage.googleapis.com/v0/b/memesticker-7d15c.appspot.com/o/animated-meme-thumb%2F0030.jpg?alt=media&token=fa7c124a-c1ed-4398-bfb5-1a19582de152","0030"});

        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{MemeContract.MemeEntry.COL_IMAGE,"2002"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{MemeContract.MemeEntry.COL_IMAGE,"2013"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{MemeContract.MemeEntry.COL_IMAGE,"2025"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{MemeContract.MemeEntry.COL_IMAGE,"2026"});
        db.execSQL(DATABASE_UPDATE_7_8_B, new Object[]{MemeContract.MemeEntry.COL_IMAGE,"2028"});

    }
    private void version9(SQLiteDatabase db){
        db.execSQL(DATABASE_UPDATE_8_9, new Object[]{"2002","2002"});
        db.execSQL(DATABASE_UPDATE_8_9, new Object[]{"2013","2013"});
        db.execSQL(DATABASE_UPDATE_8_9, new Object[]{"2025","2025"});
        db.execSQL(DATABASE_UPDATE_8_9, new Object[]{"2026","2026"});
        db.execSQL(DATABASE_UPDATE_8_9, new Object[]{"2028","2028"});
    }
}
