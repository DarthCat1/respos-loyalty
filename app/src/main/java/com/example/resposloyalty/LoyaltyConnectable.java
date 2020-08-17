package com.example.resposloyalty;

import org.ksoap2.transport.HttpTransportSE;

public interface LoyaltyConnectable {
    String wsdlUrl = "http://dev.respos.trade/service3/1cm?wsdl";
    String nameSpace = "http://ws1mpServer.module.mp/";
    HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
}
