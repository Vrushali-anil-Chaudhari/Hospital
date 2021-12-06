package com.vrushali.hospital.Email;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.vrushali.hospital.R;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailApi extends AsyncTask<Void, Void,Void> {
private Context context;
private Session session;
private String email,subject,message;

    public JavaMailApi(Context context, String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }
ProgressDialog progressDialog;
    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please wait as the email is being sent>>");
        progressDialog.setMessage("Sending......");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties=new Properties();
        properties.put("mail.smtp.host","smpt.gmail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.port","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");
        session=Session.getDefaultInstance(properties,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(Util.Email,Util.PASSWORD);
            }

        });
        MimeMessage mineMessage=new MimeMessage(session);
        try {
            mineMessage.setFrom(new InternetAddress(Util.Email));
            mineMessage.addRecipients(Message.RecipientType.TO,String.valueOf(new InternetAddress(email)));
            mineMessage.setSubject(subject);
            mineMessage.setText(message);
            Transport.send(mineMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        startAlertDialog();
        super.onPostExecute(aVoid);
    }

    private void startAlertDialog() {
        AlertDialog.Builder myDialog= new AlertDialog.Builder(context);
        LayoutInflater inflater= LayoutInflater.from(context);
        View myView= inflater.inflate(R.layout.output_layout,null);
        myDialog.setView(myView);
        final AlertDialog dialog= myDialog.create();
        dialog.setCancelable(false);
        Button close=myView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
