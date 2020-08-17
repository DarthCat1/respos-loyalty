package com.example.resposloyalty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientCard {
    private String cardCode;
    private String extCardCode; //external card code
    private byte cardType;  //  0 - RFID, 1 - EAN13
    private String status;  //  0 - зарегистрирована; 1 - активная; 2 -заблокированная;
                            // 3 – выдана; 4 – списана;
    private Date registrationDate;
    private Date activateDate;
    private String cardGuid;
    private String cardCategory;

    protected ClientCard(String info) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            this.activateDate = dateFormat.parse(getParamValue("activeDate", info));
            this.registrationDate = dateFormat.parse(getParamValue("alterDate", info));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.cardCategory = (getParamValue("cardCategory", info));

        this.extCardCode = getParamValue("extCard", info);

        this.cardGuid = getParamValue("extCardg", info);

        this.cardCode = getParamValue("intCard", info);

        this.cardType = Byte.valueOf(getParamValue("kindCard", info));

        this.status = getParamValue("status", info);
    }

    public String getCardCode() {
        return cardCode;
    }

    public String getExtCardCode() {
        return extCardCode;
    }

    public byte getCardType() {
        return cardType;
    }

    public String getStatus() {
        if (status.equals("0"))
            return "Зареєстрована";
        if (status.equals("1"))
            return "Активна";
        if (status.equals("2"))
            return "Заблокована";
        if (status.equals("3"))
            return "Видана(необхідно заповнити профіль)";
        if (status.equals("4"))
            return "Списана";
        return "";
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Date getActivateDate() {
        return activateDate;
    }

    public String getCardGuid() {
        return cardGuid;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    //  get parameter value from string information
    private String getParamValue(String parameterName, String cardInfo) {
        Pattern pattern = Pattern.compile(parameterName + "=.+?;");
        Matcher matcher = pattern.matcher(cardInfo);
        matcher.find();
        String cardParamInfo = "";
        try {
            cardParamInfo = cardInfo.substring(matcher.start(), matcher.end());
            cardParamInfo = cardParamInfo.substring(cardParamInfo.indexOf('=') + 1, cardParamInfo.indexOf(';'));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return cardParamInfo.equals("anyType{}") ? "" : cardParamInfo;
    }

    @Override
    public String toString() {
        return cardCode;
    }
}
