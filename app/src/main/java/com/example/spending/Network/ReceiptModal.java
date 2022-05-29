package com.example.spending.Network;

public class ReceiptModal {
    //constructor
    public ReceiptModal(String url) {
        this.url= url;
    }
    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
