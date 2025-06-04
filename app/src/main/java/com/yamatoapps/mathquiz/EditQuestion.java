package com.yamatoapps.mathquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditQuestion extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener{

    Button btnSaveQuestion, btnSeeQuestions;
    TextView tvA,tvB,tvC,tvD,tvQuestion;
    androidx.appcompat.widget.Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] choices = {"A","B","C","D"};
    Spinner answerSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        btnSaveQuestion = findViewById(R.id.btnSaveQuestion);
        toolbar = findViewById(R.id.toolbar);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        answerSpinner = (Spinner) findViewById(R.id.answerSpinner);
        answerSpinner.setOnItemSelectedListener(this);

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Saving your data.");
        progressDialog.setMessage("Please wait");

        tvQuestion = findViewById(R.id.tvQuestion);
        tvA = findViewById(R.id.tvA);
        tvB = findViewById(R.id.tvB);
        tvC = findViewById(R.id.tvC);
        tvD = findViewById(R.id.tvD);


        String question_id = getIntent().getStringExtra("question_id");

        db.collection("math_questions").document(question_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot questionDocument) {
                Question question;
                question = new Question(questionDocument.getString("question"),
                        questionDocument.getString("answer"),
                        questionDocument.getString("a"),
                        questionDocument.getString("b"),
                        questionDocument.getString("c"),
                        questionDocument.getString("d"),
                        questionDocument.getId(
                        ));
                tvQuestion.setText(question.question);
                tvA.setText(question.a);
                tvB.setText(question.b);
                tvC.setText(question.c);
                tvD.setText(question.d);
            }
        });
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);

        alertDialogBuilder.setMessage("Operation Success");
        alertDialogBuilder.setTitle("Data updated successfully!");
        alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
            progressDialog.dismiss();
            dialogInterface.dismiss();
        });

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,choices);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        answerSpinner.setAdapter(aa);

        btnSaveQuestion.setOnClickListener(view -> {
            progressDialog.show();
            progressDialog.setMax(11);
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("question",tvQuestion.getText().toString());
            progressDialog.incrementProgressBy(1);
            data.put("a",tvA.getText().toString());
            progressDialog.incrementProgressBy(1);
            data.put("b",tvB.getText().toString());
            progressDialog.incrementProgressBy(1);
            data.put("c",tvC.getText().toString());
            progressDialog.incrementProgressBy(1);
            data.put("d",tvD.getText().toString());
            progressDialog.incrementProgressBy(1);
            data.put("answer",choices[answerSpinner.getSelectedItemPosition()].toString());
            progressDialog.incrementProgressBy(1);
            db.collection("math_questions").document(question_id).update(data).addOnSuccessListener( documentReference -> {
                try {
                    progressDialog.setProgress(11);
                    Thread.sleep(3000);
                    alertDialogBuilder.create().show();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}