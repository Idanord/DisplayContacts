package com.example.will.displaycontacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayContactsActivity extends Activity {

    //instance variables
    private Button loadContacts;
    private TextView listContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);

        //get ref to widgets
        listContacts = findViewById(R.id.listContacts);
        loadContacts = findViewById(R.id.loadContacts);

        //set onClickListener to the button
        loadContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadContacts();
            }
        });
    }

    private void loadContacts(){

        //set local variables for StringBuilder and ContentResolver
        StringBuilder builder = new StringBuilder();
        ContentResolver contentResolver = getContentResolver();

        //set up cursor object
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);

        if(cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.
                        DISPLAY_NAME));

                //check for phone number
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex
                        (ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.
                            CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.
                            CONTACT_ID + " =?", new String[]{id}, null);

                    while (cursor2.moveToNext()) {
                        String phoneNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.
                                CommonDataKinds.Phone.NUMBER));
                        builder.append("Contact : ").append(name).append(", Phone Number :")
                                .append(phoneNumber).append("\n\n");
                    }

                    cursor2.close();
                }

            }
        }

        cursor.close();

        //set contacts to list
        listContacts.setText(builder.toString());
    }
}
