package com.example.resposloyalty;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientProfile implements LoyaltyConnectable, Runnable {

    private Authenticator auth;
    private String lastName;
    private String firstName;
    private String middleName;
    private Date birthday;
    private boolean isMale;
    private String address;
    private String phoneNumber;
    private String email;
    private String errorMessage;

    public ClientProfile(Authenticator auth) {
        this.auth = auth;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void receiveProfileInfo() {
        String soapAction = "http://ws1mpServer.module.mp/#LoyallMobpImplService:TransferUserProfile";
        String methodName = "TransferUserProfile";
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        soapObject.addProperty("TOKEN", auth.getToken());
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        try {
            hts.call(soapAction, envelope);
            SoapObject responseObject = (SoapObject) envelope.getResponse();
            firstName = responseObject.getProperty(0).toString();
            firstName = firstName.equals("anyType{}") ? "" : firstName;
            lastName = responseObject.getProperty(1).toString();
            lastName = lastName.equals("anyType{}") ? "" : lastName;
            middleName = responseObject.getProperty(2).toString();
            middleName = middleName.equals("anyType{}") ? "" : middleName;
            isMale = responseObject.getProperty(3).toString().equals("0") ? false : true;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthday = sdf.parse(responseObject.getProperty(4).toString());

            address = responseObject.getProperty(5).toString();
            address = address.equals("anyType{}") ? "" : address;
            phoneNumber = responseObject.getProperty(6).toString();
            phoneNumber = phoneNumber.equals("anyType{}") ? "" : phoneNumber;
            email = responseObject.getProperty(7).toString();
            email = email.equals("anyType{}") ? "" : email;
            errorMessage = responseObject.getProperty(8).toString();
            errorMessage = errorMessage.equals("anyType{}") ? "" : errorMessage;

        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void updateProfile() {
        String methodName = "UpdateUserProfile";
        String soapAction = "http://ws1mpServer.module.mp/#LoyallMobpImplService:UpdateUserProfile";
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        soapObject.addProperty("TOKEN", auth.getToken());
        soapObject.addProperty("FIRSTNAME", this.getFirstName());
        soapObject.addProperty("LASTNAME", this.getLastName());
        soapObject.addProperty("MIDDLENAME", this.getMiddleName());
        soapObject.addProperty("GENDER", this.isMale() == true ? "1" : "0");

        PropertyInfo pi = new PropertyInfo();
        pi.setName("BIRTH");
        pi.setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(this.getBirthday()));
        soapObject.addProperty(pi);

        soapObject.addProperty("ADDRESS", this.getAddress());
        soapObject.addProperty("PHONENUMBER", this.getPhoneNumber());
        soapObject.addProperty("EMAIL", this.getEmail());

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        try {
            hts.call(soapAction, envelope);
            SoapObject responseObject = (SoapObject) envelope.getResponse();
            if (responseObject.getProperty("Res").toString().equals("false"))
                errorMessage = responseObject.getProperty("MESS").toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    boolean isValidFirstName(String name) {
        if (!name.matches("[a-zA-Zа-яА-Я]+")) {
            this.errorMessage += "Ім'я повинно складатися тільки з літер\n";
            return false;
        }
        return true;
    }

    boolean isValidLastName(String name) {
        if (!name.matches("[a-zA-Zа-яА-Я]+")) {
            this.errorMessage += "Прізвище повинно складатися тільки з літер\n";
            return false;
        }
        return true;
    }

    boolean isValidBirthday(Date birthday) {
        if (birthday.compareTo(new Date(0)) == 0) {
            this.errorMessage += "Встановіть дату народження\n";
            return false;
        }
        //  check if birthday was 18 or more years ago
        else if ((new Date().getTime() - birthday.getTime()) < 568025136000l) {
            this.errorMessage += "Ваш вік повинен бути більше 18 років\n";
            return false;
        }

        return true;
    }

    boolean isValidPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("\\+380\\d{9}")) {
            this.errorMessage += "Формат номера телефону +380ХХХХХХХХХ\n";
            return false;
        }
        return true;
    }

    boolean isValidEmail(String email) {
        if (!email.matches("\\w+@[a-z]+\\.[a-z]+")) {
            this.errorMessage += "Невірний формат електронної пошти\n";
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        receiveProfileInfo();
    }
}
