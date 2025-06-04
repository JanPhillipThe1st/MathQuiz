package com.yamatoapps.mathquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;

public class PlayGame extends AppCompatActivity {

    Button btnProceed;
    TextView tvA,tvB,tvC,tvD,tvQuestion,tvScore;
    ArrayList<Question> questions;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] choices = {"A","B","C","D"};
    Spinner answerSpinner;

    int i = 0,score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        btnProceed = findViewById(R.id.btnProceed);

        tvA = findViewById(R.id.tvA);
        tvB = findViewById(R.id.tvB);
        tvC = findViewById(R.id.tvC);
        tvD = findViewById(R.id.tvD);
        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        answerSpinner = (Spinner) findViewById(R.id.answerSpinner);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,choices);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        answerSpinner.setAdapter(aa);

        questions = new ArrayList<Question>();
        db.collection("math_questions").get().addOnSuccessListener((questionSnapshot) -> {

            questions.clear();

            for (DocumentSnapshot questionDocument:questionSnapshot){
                Question question;
                question = new Question(
                        questionDocument.getString("question"),
                        questionDocument.getString("answer"),
                        questionDocument.getString("a"),
                        questionDocument.getString("b"),
                        questionDocument.getString("c"),
                        questionDocument.getString("d"),
                        questionDocument.getId()

                );
                questions.add(question);
            }
            tvQuestion.setText(questions.get(i).question);
            tvA.setText(questions.get(i).a);
            tvB.setText(questions.get(i).b);
            tvC.setText(questions.get(i).c);
            tvD.setText(questions.get(i).d);
            Log.d("Answer",questions.get(i).answer);
            tvScore.setText(score + " / " + questions.size());
            if ((i+1) >= questions.size() ) {
                btnProceed.setText("Finish");
            }
            else{
                btnProceed.setText("Next");
            }
            btnProceed.setOnClickListener(view -> {
                if ((i+1) >= questions.size() ) {
                    btnProceed.setText("Finish");
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
                    alertDialogBuilder.setTitle("Quiz done");
                    if(choices[answerSpinner.getSelectedItemPosition()].toString().contains(questions.get(i).answer)){
                        score ++;
                    }
                    alertDialogBuilder.setMessage("Congratulations! your score is:\n"+score + " / " + questions.size());
                    alertDialogBuilder.setPositiveButton("OK",(dialogInterface, i1) -> {
                        dialogInterface.dismiss();
                        finish();
                    });

                    alertDialogBuilder.create().show();
                }
                else{
                    Log.d("Answer",questions.get(i).answer);
                    if(choices[answerSpinner.getSelectedItemPosition()].toString().contains(questions.get(i).answer)){
                        score ++;
                    }
                    btnProceed.setText("Next");
                    i++;
                    tvQuestion.setText(questions.get(i).question);
                    tvA.setText(questions.get(i).a);
                    tvB.setText(questions.get(i).b);
                    tvC.setText(questions.get(i).c);
                    tvD.setText(questions.get(i).d);


                }
                tvScore.setText(score + " / " + questions.size());

            });

        });






    }
    }
