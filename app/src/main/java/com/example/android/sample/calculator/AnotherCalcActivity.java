package com.example.android.sample.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AnotherCalcActivity extends AppCompatActivity implements TextWatcher,View.OnClickListener{

    /**
     * 上の計算ボタンを押した時のリクエストコード
     */
    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;

    /**
     * 下の計算ボタンを押した時のリクエストコード
     */
    private static final int REQUEST_CODE_ANOTHER_CALC_2 = 2;

    /**
     * 計算に使う数値のテキストボックス1。
     */
    private EditText numberInout1;

    /**
     * 計算に使う数値のテキストボックス2。
     */
    private EditText numberInout2;

    /**
     * 演算子のセレクトボックス。
     */
    private Spinner operatorSelector;

    /**
     * 計算結果のラベル。
     */
    private TextView calcResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_calc);

        findViewById(R.id.backButton).setOnClickListener(this);

        numberInout1 = (EditText)findViewById(R.id.numberInput1);
        numberInout1.addTextChangedListener(this);

        numberInout2 = (EditText)findViewById(R.id.numberInput2);
        numberInout2.addTextChangedListener(this);

        operatorSelector = (Spinner) findViewById(R.id.operationSelector);
        calcResult = (TextView) findViewById(R.id.calcResult);
    }

    @Override
    public void onClick(View v) {
        //どちらかのEditTextに値が入っていない場合
        if(!checkEditTextInput()){
            //キャンセルとみなす
            setResult(RESULT_CANCELED);
        }else{
            //計算結果
            int result = calc();

            //インテントを生成し、計算結果を詰める
            Intent data = new Intent();
            data.putExtra("result",result);
            setResult(RESULT_OK,data);
        }

        // アクティビティを終了
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 結果がOKでない場合、何もしない
        if(resultCode != RESULT_OK) return;

        // 結果データセットを取り出す
        Bundle resultBundle = data.getExtras();

        // 結果データセットに、所定のキーが含まれていない場合、何もしない
        if(resultBundle.containsKey("result")) return;

        //結果データから、resultキーに対応するint値を取り出す
        int result = resultBundle.getInt("result");

        if(requestCode == REQUEST_CODE_ANOTHER_CALC_1){
            // 上の計算ボタンを押した後、戻ってきた場合
            numberInout1.setText(String.valueOf(result));
        }else if(requestCode == REQUEST_CODE_ANOTHER_CALC_2){
            // 下の計算ボタンを押した後、戻ってきた場合
            numberInout2.setText(String.valueOf(result));
        }

        //計算をし直して、結果を表示する
        refreshResult();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // テキストが変更された後に呼ばれる。sは変更後の内容で編集可能
        // 必要があれば計算を行い、結果を表示する
        refreshResult();
    }

    /**
     * 二つのEditTextに入力がされているかをチェックする。
     * @return 判定結果
     */
    private boolean checkEditTextInput(){
        // 入力内容を取得する
        String input1 = numberInout1.getText().toString();
        String input2 = numberInout2.getText().toString();
        // 判定する
        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }

    /**
     * 計算結果の表示を更新する。
     */
    private void refreshResult(){
        if(checkEditTextInput()){
            //計算を行う
            int result = calc();

            //計算結果用のTextViewを書き換える
            String resultText = getString(R.string.calc_resut_text,result);
            calcResult.setText(resultText);
        }else{
            //どちらかが入力されていない状態の場合、計算結果用の表示をデフォルトに戻す
            calcResult.setText(R.string.calc_result_default);
        }
    }

    /**
     * 計算を行う。
     * @return 計算結果
     */
    private int calc(){
        //入力内容を取得する
        String input1 = numberInout1.getText().toString();
        String input2 = numberInout2.getText().toString();

        //int型に変換する
        int number1 = Integer.valueOf(input1);
        int number2 = Integer.valueOf(input2);

        //Spinnerから、選択中のindexを取得する
        int operator = operatorSelector.getSelectedItemPosition();

        //indexに応じて計算結果を返す
        switch(operator) {
            case 0:
                return number1 + number2;
            case 1:
                return number1 - number2;
            case 2:
                return number1 * number2;
            case 3:
                return number1 / number2;
            default:
                //通常発生しない
                throw new RuntimeException();
        }
    }
}
