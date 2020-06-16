package com.example.marieniel.registration;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.*;


public class RegistrationActivity extends AppCompatActivity {
    DatabaseHelper myDB;

    EditText fnameEditText, companyEditText, cnumEditText,emailEditText;
    Spinner designationSpinner;
    CheckBox updateCheckBox;
    Button submitBtn;
    Button showBtn;
    Button dbBtn;
    String checkboxcon;
//for drop down (declaration of array) (MAIN CLASS)
    ArrayList<String> desig=new ArrayList<String>();

//manifest declaration ... (MAIN CLASS)
    private static  final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

//manifest checking ng permission (MAIN CLASS)
    public static void verifyStoragePermissions(Activity activity){
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(writePermission !=PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            ); }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        myDB = new DatabaseHelper(this);

        fnameEditText = (EditText) findViewById(R.id.fnameEditText);
        companyEditText = (EditText) findViewById(R.id.companyEditText);
        cnumEditText = (EditText) findViewById(R.id.cnumEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        updateCheckBox = (CheckBox) findViewById(R.id.updateCheckBox);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        showBtn = (Button) findViewById(R.id.showBtn);
        dbBtn = (Button) findViewById(R.id.dbBtn);


//spinner combobox drop down (ON CREATE)

        desig.add("Engineer");
        desig.add("Owner");
        desig.add("Student");
        desig.add("Constructor");
        desig.add("Others");


        designationSpinner = (Spinner) findViewById(R.id.designationSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, desig);
        designationSpinner.setAdapter(adapter);


// to initialize other methods (ON CREATE)
        addDATA();
        showDATA();
        //manifest called here in "oncreate"
        verifyStoragePermissions(this);




//extraction button (function) (ON CREATE)
         dbBtn.setOnClickListener(new View.OnClickListener() {
                SQLiteDatabase sqldb = myDB.getReadableDatabase();
                Cursor c = null;
                @Override
                public void onClick(View v) {
                    try{
                        c = sqldb.rawQuery("select * from customerinfo_tbl", null);
                        int rowcount = 0;
                        int colcount = 0;
                        File sdCardDir = Environment.getExternalStorageDirectory();
                        String filename = "customerinfo.csv";
                        File saveFile = new File(sdCardDir,filename);
                        FileWriter fw = new FileWriter(saveFile);

                        BufferedWriter bw = new BufferedWriter(fw);
                        rowcount = c.getCount();
                        colcount = c.getColumnCount();
//for CSV inputss
                     if (c !=null && c.moveToFirst()){
//get column name .....................
                         if (rowcount>0){
                             c.moveToFirst();
                             for (int i=0;i<colcount;i++){
                                 if (i != colcount -1){
                                     bw.write(c.getColumnName(i)+",");
                                 }
                                 else{
                                     bw.write(c.getColumnName(i));
                                 }
                             }
                             bw.newLine();}
// loop for extracting all columns and rows...............
                         do{
                             bw.write(c.getString(0)+ ","
                                     + c.getString(1)+ ","
                                     + c.getString(2)+ ","
                                     + c.getString(3)+ ","
                                     + c.getString(4)+ ","
                                     + c.getString(5)+ ","
                                     + c.getString(6)+ ","
                                     + c.getString(7));
                             bw.newLine();
                         } while (c.moveToNext());

                     }
                      bw.flush();
                     // dbBtn.setText("Exported Successfully.");

                  } catch (Exception ex) {
                      if (sqldb.isOpen()){
                          sqldb.close();
                       //   dbBtn.setText(ex.getMessage().toString());
                      }
                  } finally {

                  }
              }

          });
// end curly brace ng on create method
    }



// add button (ANOTHER METHOD)
    public void addDATA(){
        submitBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //VALIDATIOOOOOOOOOOOOOOOOOOOOOONNNNN
                            checkDataEntered();
                        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        //CHECKING NG VALIDATION, IF NULL THEN DI SYA MAG IINSERT SA DATABASE
                       if (fnameEditText.getError() !=null || companyEditText.getError() !=null || cnumEditText.getError() !=null || emailEditText.getError() !=null){
                           Toast.makeText(RegistrationActivity.this, "Please check all fields.", Toast.LENGTH_LONG).show();
                       }

                       else {
                           //SUBMITTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT

                                 //check if Checkbox true or false
                                    if (updateCheckBox.isChecked())
                                          checkboxcon = "true";
                                    else
                                           checkboxcon = "false";
                                 //insert command
                           boolean isInserted = myDB.insertDATA(fnameEditText.getText().toString(), companyEditText.getText().toString(), designationSpinner.getSelectedItem().toString(), cnumEditText.getText().toString(), emailEditText.getText().toString(), checkboxcon, timeStamp);
                           if (isInserted = true)
                               //call this method to open thank you activity
                               thankyouactivity();
                              // Toast.makeText(RegistrationActivity.this, "Submitted!", Toast.LENGTH_LONG).show();
                           else
                               Toast.makeText(RegistrationActivity.this, "Please check all fields.", Toast.LENGTH_LONG).show();

                           //clear all fields after submition
                           fnameEditText.setText("");
                           companyEditText.setText("");
                           cnumEditText.setText("");
                           emailEditText.setText("");
                           updateCheckBox.setChecked(false);

                            }

                    }
                }
                );
    }


//OPEN @2ND ACTIVITY METHOD
    public void thankyouactivity(){
        Intent myintent = new Intent(this,ThankYouActivity.class);
        startActivity(myintent);
        finish();
    }

//VALIDATION CODESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
                //email validation
        boolean isEmail (EditText text){
            CharSequence email = text.getText().toString();
            return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        }
        //text validation if empty
        boolean isEmpty (EditText text){
            CharSequence str = text.getText().toString();
            return TextUtils.isEmpty(str);
        }
        //phone number validation
        boolean isNumber(EditText text){
            CharSequence num = text.getText().toString();
            return !TextUtils.isEmpty(num) && ((num.length()==11) || (num.length()==7)) ;
        }

        void checkDataEntered(){
            if (isEmpty(fnameEditText)){
                fnameEditText.setError("This field is required");
            }
            if (isEmpty(companyEditText)){
            companyEditText.setError("This field is required");
        }
        if (isEmail(emailEditText)== false){
            emailEditText.setError("Please enter a valid email");
        }
        if (isNumber(cnumEditText)== false){
            cnumEditText.setError("Please enter a valid contact number.");
        }

    }
//end of validation



//show button (ANOTHER METHOD)
    public void showDATA(){
        showBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //SHOWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
                        Cursor result = myDB.getAllData();
                        if (result.getCount()== 0){
                            //show message
                            showMessage("Error","Empty notes");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(result.moveToNext()){
                            buffer.append("Full Name : "+ result.getString(1)+"\n");
                            buffer.append("Company : "+ result.getString(2)+"\n");
                            buffer.append("Designation : "+ result.getString(3)+"\n");
                            buffer.append("Contact Number : "+ result.getString(4)+"\n");
                            buffer.append("Email : "+ result.getString(5)+"\n");
                            buffer.append("Enable Update : "+ result.getString(6)+"\n\n");
                        }
                        //show all data
                        showMessage("Customer's Information",buffer.toString());
                    }
                }
        );
    }

//pop up show message(ANOTHER METHOD)
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
