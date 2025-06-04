package com.yamatoapps.mathquiz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuestionAdapter extends ArrayAdapter<Question> {


    public QuestionAdapter(@NonNull Context context, @NonNull ArrayList<Question> objects) {
        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Question item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.question_card, parent, false);
        }
        TextView tvQuestion = (TextView)convertView.findViewById(R.id.tvQuestion);
        TextView tvAnswer = (TextView)convertView.findViewById(R.id.tvAnswer);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        tvAnswer.setText("Answer: "+item.answer);
        tvQuestion.setText("Question: "+item.question);

        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(parent.getContext());
        alertDialogBuilder.setMessage("Deleting data");
        alertDialogBuilder.setTitle("Are you sure you want to delete this question? This cannot be undone.");
        alertDialogBuilder.setPositiveButton("NO",(dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        alertDialogBuilder.setNegativeButton("YES",(dialogInterface, i) -> {
            db.collection("math_questions").document(item.id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    MaterialAlertDialogBuilder deleteDialog = new MaterialAlertDialogBuilder(parent.getContext());
                    deleteDialog.setTitle("Operation success!");
                    deleteDialog.setMessage("Data successfully deleted!");
                    deleteDialog.setPositiveButton("OK",(deleteDialogInterface, i) -> {
                        deleteDialogInterface.dismiss();
                    });
                    dialogInterface.dismiss();

                }
            });
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(parent.getContext(),EditQuestion.class);
                editIntent.putExtra("question_id",item.id);
                parent.getContext().startActivity(editIntent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            alertDialogBuilder.create().show();
            }
        });
        return convertView;

    }
}
