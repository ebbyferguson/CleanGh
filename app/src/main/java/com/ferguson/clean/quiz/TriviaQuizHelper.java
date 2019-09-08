package com.ferguson.clean.quiz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


class TriviaQuizHelper extends SQLiteOpenHelper {



    private Context context;
    private static final String DB_NAME = "TQuiz.db";

    //If you want to add more questions or wanna update table values
    //or any kind of modification in db just increment version no.
    private static final int DB_VERSION = 3;
    //Table name
    private static final String TABLE_NAME = "TQ";
    //Id of question
    private static final String UID = "_UID";
    //Question
    private static final String QUESTION = "QUESTION";
    //Option A
    private static final String OPTA = "OPTA";
    //Option B
    private static final String OPTB = "OPTB";
    //Option C
    private static final String OPTC = "OPTC";
    //Option D
    private static final String OPTD = "OPTD";
    //Answer
    private static final String ANSWER = "ANSWER";
    //So basically we are now creating table with first column-id , sec column-question , third column -option A, fourth column -option B , Fifth column -option C , sixth column -option D , seventh column - answer(i.e ans of  question)
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + UID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + QUESTION + " VARCHAR(255), " + OPTA + " VARCHAR(255), " + OPTB + " VARCHAR(255), " + OPTC + " VARCHAR(255), " + OPTD + " VARCHAR(255), " + ANSWER + " VARCHAR(255));";
    //Drop table query
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    TriviaQuizHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //OnCreate is called only once
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //OnUpgrade is called when ever we upgrade or increment our database version no
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    void allQuestion() {
        ArrayList<TriviaQuestion> arraylist = new ArrayList<>();

        arraylist.add(new TriviaQuestion("How much plastics do you think we consume (eat) a day?", "The size of a foot ball", "The size of a bank card", "The size of a Nokia phone", "The size of pure water sachet", "The size of a bank card"));

        arraylist.add(new TriviaQuestion("How long is the coastline of Ghana?", "560 kilometers", "400 kilometers", "670 kilometers", "340 kilometers", "560 kilometers"));

        arraylist.add(new TriviaQuestion("How much plastics enter the Ocean every year?", "8 million tonnes", "4 million tonnes", "6 billion tonnes", "20 million tonnes", "8 million tonnes"));

        arraylist.add(new TriviaQuestion("Plastic pollution costs the world up how much per annum?", "$40 trillion", "$2.5 trillion", "$3 trillion", "$10 trillion", "$2.5 trillion"));

        arraylist.add(new TriviaQuestion("How much plastic entered the ocean in 2010 alone?", "60 million tonnes of plastics", "11 million tonnes of plastics", "12 million tonnes of plastics", "15 million tonnes of plastics", "12 million tonnes of plastics"));

        arraylist.add(new TriviaQuestion("When was plastic first invented?", "1907", "1945", "1957", "1990", "1907"));

        arraylist.add(new TriviaQuestion("How many tonnes of plastic is produced each year?", "200 million", "6 million", "350 million", "6 billion", "350 million"));

        arraylist.add(new TriviaQuestion("What's the percentage of plastics in marine litter?", "75% - 100%", "45% - 60%", "15% - 30%", "50% - 70%", "75% - 100%"));

        arraylist.add(new TriviaQuestion("How long do you think it takes for plastic to break down? It's estimated it could take up to:", "hundreds of years to degrade", "thousands of years to degrade", "a decade to degrade", "a million years to degrade", "thousands of years to degrade"));

        arraylist.add(new TriviaQuestion("Which creatures are highly susceptible to plastic ingestion?", "Fish", "Mammals", "Reptiles", "Birds", "Birds"));

        arraylist.add(new TriviaQuestion("What sea animal eats plastic bags because they mistake them for jellyfish?", "Sea horse", "Whales", "Turtles", "Fish", "Turtles"));

        arraylist.add(new TriviaQuestion("Global plastic production is currently growing by approximately __________every year.", "30%", "4%", "6%", "10%", "4%"));

        arraylist.add(new TriviaQuestion("Which country in Africa has been able to successfully ban plastics? ", "Ghana", "Nigeria", "Uganda", "Rwanda", "Rwanda"));

        this.addAllQuestions(arraylist);

    }


    private void addAllQuestions(ArrayList<TriviaQuestion> allQuestions) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (TriviaQuestion question : allQuestions) {
                values.put(QUESTION, question.getQuestion());
                values.put(OPTA, question.getOptA());
                values.put(OPTB, question.getOptB());
                values.put(OPTC, question.getOptC());
                values.put(OPTD, question.getOptD());
                values.put(ANSWER, question.getAnswer());
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    List<TriviaQuestion> getAllOfTheQuestions() {

        List<TriviaQuestion> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {UID, QUESTION, OPTA, OPTB, OPTC, OPTD, ANSWER};
        Cursor cursor = db.query(TABLE_NAME, coloumn, null, null, null, null, null);


        while (cursor.moveToNext()) {
            TriviaQuestion question = new TriviaQuestion();
            question.setId(cursor.getInt(0));
            question.setQuestion(cursor.getString(1));
            question.setOptA(cursor.getString(2));
            question.setOptB(cursor.getString(3));
            question.setOptC(cursor.getString(4));
            question.setOptD(cursor.getString(5));
            question.setAnswer(cursor.getString(6));
            questionsList.add(question);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return questionsList;
    }
}
