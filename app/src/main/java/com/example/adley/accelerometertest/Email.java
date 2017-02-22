package com.example.adley.accelerometertest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by adley on 2/7/2016.
 */
public class Email {

    public Email(){

    }
    public static void sendEmailWithMultipleAttachments(Context context, String emailTo, String emailCC, String subject, String emailText, ArrayList<String> filePaths){
        final Intent emailIntent=new Intent(Intent.ACTION_SEND_MULTIPLE);   //Allows user to send email with multiple file attachments
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_CC, new String[]{emailCC});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);

        ArrayList<Uri> uris=new ArrayList<Uri>();

        for(String fileName:filePaths){
            File fileAttachment=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
            fileAttachment.setReadable(true,false);
            Uri u = Uri.fromFile(fileAttachment);
            uris.add(u);
        }

        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(emailIntent,"Pick email/Send email"));
    }

    public static void sendEmailWithMultipleAttachments(Context context, ArrayList<String> filePaths){
        final Intent emailIntent=new Intent(Intent.ACTION_SEND_MULTIPLE);   //Allows user to send email with multiple file attachments
        emailIntent.setType("text/plain");

        ArrayList<Uri> uris=new ArrayList<Uri>();

        for(String fileName:filePaths){
            File fileAttachment=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
            fileAttachment.setReadable(true,false);
            Uri u = Uri.fromFile(fileAttachment);
            uris.add(u);
        }

        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(emailIntent,"Pick email/Send email"));
    }
}
