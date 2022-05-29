package com.example.spending.data.model;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class ReceiptDetail {
    public ReceiptDetail(String user_id, ArrayList<IndividualItem> items, long date, String imageUrl,String category) {
        this.user_id = user_id;
        this.items = items;
        this.timestamp = date;
        this.imageUrl = imageUrl;
        this.category=category;
    }

    public ReceiptDetail() {
        this.user_id=null;
        this.items=null;
        this.timestamp=0;
        this.imageUrl=null;
        this.category=null;

    }

    public String getUser_id() {
        return user_id;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<IndividualItem> getItems() {
        return items;
    }
    private String user_id;
    private ArrayList<IndividualItem> items;


    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    private String imageUrl;
    private String category;

    public String getCategory() {
        return category;
    }

    public static class IndividualItem{
        public IndividualItem(String label, String ocr_text) {
            this.label = label;
            this.ocr_text = ocr_text;
        }
        public IndividualItem(){
            this.label=null;
            this.ocr_text=null;
        }

        public String getLabel() {
            return label;
        }

        public String getOcr_text() {
            return ocr_text;
        }

        private String label;
        private String ocr_text;

    }
}
