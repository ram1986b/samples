package com.covisint.papi.sample.android.openregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.covisint.papi.sample.android.openregistration.model.contact.Address;
import com.covisint.papi.sample.android.openregistration.model.person.Person;
import com.covisint.papi.sample.android.openregistration.util.Constants;
import com.covisint.papi.sample.android.openregistration.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class AddressInputActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private Button mAddressSubmissionButton;
    private View mUserInfoSubmissionFormView;
    private Person mPerson;
    private EditText mAddress1ET;
    private EditText mAddress2ET;
    private EditText mAddress3ET;
    private EditText mCity;
    private EditText mState;
    private EditText mPostalCode;
    private Spinner mCountriesSpinner;

    // UI references.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_input);

        String personJson = getIntent().getStringExtra(Constants.PERSON_JSON);
        Gson gson = new GsonBuilder().create();
        mPerson = gson.fromJson(personJson, Person.class);

        mUserInfoSubmissionFormView = findViewById(R.id.user_info_submission_form);

        mAddress1ET = (EditText)findViewById(R.id.address1);
        mAddress2ET = (EditText)findViewById(R.id.address2);
        mAddress3ET = (EditText)findViewById(R.id.address3);
        mCity = (EditText)findViewById(R.id.city);
        mState = (EditText)findViewById(R.id.state);
        mPostalCode = (EditText)findViewById(R.id.postal_code);

        mCountriesSpinner = (Spinner) findViewById(R.id.country);
        List<String> countriesList = Utils.getCountries();
        int usIndex = countriesList.indexOf("United States");
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countriesList);
        countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountriesSpinner.setAdapter(countriesAdapter);
        mCountriesSpinner.setSelection(usIndex);

        mAddressSubmissionButton = (Button) findViewById(R.id.address_submission_button);
        mAddressSubmissionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSubmission();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptSubmission() {
        Address[] addresses = new Address[1];
        Address address = new Address();
        addresses[0] = address;
        ArrayList<String> streets = new ArrayList<>(3);
        String address1 = mAddress1ET.getText().toString();
        if (address1 != null && address1.trim().length() > 0) {
            streets.add(address1);
        } else {
            mAddress1ET.setError(getString(R.string.error_field_required));
            mAddress1ET.requestFocus();
            return;
        }
        String address2 = mAddress2ET.getText().toString();
        streets.add(address2);
        String address3 = mAddress3ET.getText().toString();
        streets.add(address3);
        address.setStreets(streets);
        String city = mCity.getText().toString();
        if (city != null && city.trim().length() > 0) {
            address.setCity(city);
        } else {
            mCity.setError(getString(R.string.error_field_required));
            mCity.requestFocus();
            return;
        }
        String state = mState.getText().toString();
        if (state != null && state.trim().length() > 0) {
            address.setState(state);
        } else {
            mState.setError(getString(R.string.error_field_required));
            mState.requestFocus();
            return;
        }
        String postalCode = mPostalCode.getText().toString();
        if (postalCode != null && postalCode.trim().length() > 0) {
            address.setPostal(postalCode);
        } else {
            mPostalCode.setError(getString(R.string.error_field_required));
            mPostalCode.requestFocus();
        }
        Object country = mCountriesSpinner.getSelectedItem();
        address.setCountry(country.toString());

        mPerson.setAddresses(addresses);

        Intent intent = new Intent(this, ContactsInputActivity.class);
        Gson gson = new GsonBuilder().create();
        String personJson = gson.toJson(mPerson);
        intent.putExtra(Constants.PERSON_JSON, personJson);
        String organizationJson = getIntent().getStringExtra(Constants.ORGANIZATION_JSON);
        intent.putExtra(Constants.ORGANIZATION_JSON, organizationJson);
        startActivity(intent);
        finish();
    }
}



