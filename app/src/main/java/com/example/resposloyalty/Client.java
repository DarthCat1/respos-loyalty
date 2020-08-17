package com.example.resposloyalty;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client implements LoyaltyConnectable, Runnable {
    List<ClientCard> clientCards = new ArrayList<>();
    String errorMessage;    // Сообщение об ошибке;
    private Authenticator auth;
    private int codeDPS;    //  Код ДПС;
    private String edrpou;  //  ЕГРПОУ
    private String inn; //  ИНН
    private String name;
    private String fullName;
    private boolean isEntity;  //  Юрлицо - 1; Физлицо - 0;
    private String licenseNumber;   //  Номер лицензии
    private boolean isMale; //  Мужской - 1;Женский — 0
    private Date birthday;
    private String address;
    private String phoneNumber;
    private String email;
    private String clientCategory;
    private String extClientCode;   //  external client code
    private double bonusCounts; // текущие бонусы клиента;
    public Client(Authenticator auth) {
        this.auth = auth;
    }

    public Authenticator getAuth() {
        return auth;
    }

    public double getBonusCounts() {
        return bonusCounts;
    }

    public void receiveClientInfo() {
        String methodName = "TransferClientCard";
        String soapAction = "http://ws1mpServer.module.mp/#LoyallMobpImplService:TransferClientCard";
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        soapObject.addProperty("TOKEN", auth.getToken());
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        try {
            hts.call(soapAction, envelope);
            SoapObject responseObject = (SoapObject) envelope.getResponse();
            codeDPS = Integer.valueOf(responseObject.getProperty(0).toString());
            edrpou = responseObject.getProperty(1).toString();
            inn = responseObject.getProperty(2).toString();
            name = responseObject.getProperty(3).toString();
            fullName = responseObject.getProperty(4).toString();
            isEntity = Byte.valueOf(responseObject.getProperty(5).toString()) == 0 ? false : true;
            licenseNumber = responseObject.getProperty(6).toString();
            isMale = Byte.valueOf(responseObject.getProperty(7).toString()) == 0 ? false : true;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthday = sdf.parse(responseObject.getProperty(8).toString());

            address = responseObject.getProperty(9).toString();
            phoneNumber = responseObject.getProperty(10).toString();
            email = responseObject.getProperty(11).toString();
            clientCategory = responseObject.getProperty(12).toString();
            extClientCode = responseObject.getProperty(13).toString();
            bonusCounts = Double.valueOf(responseObject.getProperty(14).toString());

            int i = 15;
            for (; responseObject.getProperty(i).toString().contains("extCardg"); ++i)
                clientCards.add(new ClientCard(responseObject.getProperty(i).toString()));

            errorMessage = responseObject.getProperty(i).toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public List<ClientCard> getClientCards() {
        return clientCards;
    }


    @Override
    public void run() {
        receiveClientInfo();
    }
}
