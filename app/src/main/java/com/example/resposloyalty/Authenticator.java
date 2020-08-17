package com.example.resposloyalty;


import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;

public class Authenticator implements LoyaltyConnectable, Serializable {
    private String token;
    private String errorMessage;

    public Authenticator() {
    }

    public void TransferAuth(String userName, String password) {
        String soapAction = "http://ws1mpServer.module.mp/#LoyallMobpImplService:TransferAuth";
        String methodName = "TransferAuth";
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        soapObject.addProperty("USERNAME", userName);
        soapObject.addProperty("PASSWORD", password);

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        try {
            hts.call(soapAction, envelope);
            SoapObject responseObject = (SoapObject) envelope.getResponse();
            token = responseObject.getProperty(0).toString();
            errorMessage = responseObject.getProperty(1).toString();
            errorMessage = errorMessage.equals("anyType{}") ? "" : errorMessage;
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getToken() {
        return this.token;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
