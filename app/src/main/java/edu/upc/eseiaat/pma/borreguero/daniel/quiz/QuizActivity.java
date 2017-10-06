package edu.upc.eseiaat.pma.borreguero.daniel.quiz;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.CorrectionInfo;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    public static final String CURRENT_QUESTION = "current question";
    public static final String ACIERTOS = "aciertos";
    public static final String CURRENT_ANSWER = "current answer";

    private int id_rb[]={
            R.id.answer1,R.id.answer2,R.id.answer3,R.id.answer4
    };

    private TextView text_question;
    private String[] question;
    private String[] answers;
    private String[] current_answer;
    private RadioButton[] radioButtons;
    private int iCurrentQuestion=0;
    private String correctAnswer;
    private String[] aciertos;
    private RadioGroup group;
    private Button btn_check,btn_prev;


    @Override
    protected void onStop() {
        Log.i("lifecycle","onStop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.i("lifecycle","onStart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.i("lifecycle","Destroy");
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("lifecycle","restart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("lifecycle","onSave");
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_QUESTION,iCurrentQuestion);
        outState.putStringArray(ACIERTOS,aciertos);
        outState.putStringArray(CURRENT_ANSWER,current_answer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Log.i("lifecycle","onCreate");

        radioButtons=new RadioButton[id_rb.length];
        for(int i=0;i<id_rb.length;i++){
            radioButtons[i]=(RadioButton)findViewById(id_rb[i]);
        }

        btn_check=(Button)findViewById(R.id.btn_check);
        btn_prev=(Button)findViewById(R.id.btn_previous);
        group=(RadioGroup)findViewById(R.id.answer_group);
        text_question=(TextView) findViewById(R.id.text_question);
        question=getResources().getStringArray(R.array.questions);
        answers=getResources().getStringArray(R.array.answers);

        if (savedInstanceState==null){
            empezar();
        }else{
            Bundle state=savedInstanceState;
            iCurrentQuestion=state.getInt(CURRENT_QUESTION);
            aciertos=state.getStringArray(ACIERTOS);
            current_answer=state.getStringArray(CURRENT_ANSWER);
            inizializar();
        }


        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datos();

                if (iCurrentQuestion < question.length - 1) {
                    iCurrentQuestion++;
                    inizializar();
                } else {
                    checkResult();

                }

            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (iCurrentQuestion>0){
                datos();
                iCurrentQuestion--;
                inizializar();
            }
            }
        });
    }

    private void empezar() {
        aciertos=new String[question.length];
        current_answer=new String[question.length];
        iCurrentQuestion=0;
        inizializar();
    }

    private void checkResult() {
        int correct=0,incorrect=0, noanswer=0;
        for (String b:aciertos){
            if (b.equals("true")) correct++;
            else if (b.equals("false")) incorrect++;
            else noanswer++;
        }
        String result=String.format("Correctas: %d\nIncorrectas: %d\nNo contestadas: %d\n",correct,incorrect,noanswer);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("resultado");
        builder.setCancelable(false);
        builder.setMessage(result);
        builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 empezar();
            }
        });
        builder.create().show();
    }

    private void datos() {
        int id=group.getCheckedRadioButtonId();
        if (id==-1){
            current_answer[iCurrentQuestion]="NSNC";
        }else{
            RadioButton r = (RadioButton) findViewById(id);
            String opcion = r.getText().toString();
            current_answer[iCurrentQuestion]=opcion;
        }


        if (correctAnswer.equals(current_answer[iCurrentQuestion])) {
            aciertos[iCurrentQuestion]="true";
        } else if ("NSNC".equals(current_answer[iCurrentQuestion])){
            aciertos[iCurrentQuestion]="NSNC";
        } else {
            aciertos[iCurrentQuestion]="false";
        }
    }

    private void inizializar() {
        text_question.setText(question[iCurrentQuestion]);
        String[] ans=answers[iCurrentQuestion].split(";");
        group.clearCheck();

        for (int i=0;i<id_rb.length;i++){
            radioButtons[i].setText(ans[i]);
            if (radioButtons[i].getText().equals(current_answer[iCurrentQuestion])){
                radioButtons[i].setChecked(true);
            }

        }

        correctAnswer=ans[ans.length-1];

        if (iCurrentQuestion==0){
            btn_prev.setVisibility(View.INVISIBLE);

        }else{
           btn_prev.setVisibility(View.VISIBLE);
        }

        if (iCurrentQuestion==question.length-1){
            btn_check.setText(R.string.finish);
        }else{
            btn_check.setText(R.string.check);
        }

    }
}
