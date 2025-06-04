package com.yamatoapps.mathquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManageQuestions extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);
        ListView lvQuestions = findViewById(R.id.lvQuestions);
        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(view -> {
        finish();});
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            finish();});
        ArrayList<Question> questions = new ArrayList<>();
        QuestionAdapter adapter = new QuestionAdapter(this, questions);
        db.collection("math_questions").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(DocumentSnapshot questionDocument : queryDocumentSnapshots){
                adapter.add(new Question(questionDocument.getString("question"),
                        questionDocument.getString("answer"),
                        questionDocument.getString("a"),
                        questionDocument.getString("b"),
                        questionDocument.getString("c"),
                        questionDocument.getString("d"),
                        questionDocument.getId()
                        )
                );
            }

            lvQuestions.setAdapter(adapter);
        });

    }
}