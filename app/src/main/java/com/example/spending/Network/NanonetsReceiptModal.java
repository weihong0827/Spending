package com.example.spending.Network;



import java.util.ArrayList;

public class NanonetsReceiptModal {
    //constructor
    private String message;
    private ArrayList<ReceiptResult> result;

    @Override
    public String toString() {
        return "NanonetsReceiptModal{" +
                "message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ReceiptResult> getResult() {
        return result;
    }

    public class ReceiptResult{
        @Override
        public String toString() {
            return "ReceiptResult{" +
                    "message='" + message + '\'' +
                    ", input='" + input + '\'' +
                    ", prediciton=" + prediction +
                    '}';
        }

        public String getMessage() {
            return message;
        }

        public String getInput() {
            return input;
        }

        public ArrayList<Prediction> getPrediction() {
            return prediction;
        }


        private String message;
        private String input;
        private ArrayList<Prediction> prediction;


        public class Prediction{
            @Override
            public String toString() {
                return "Prediction{" +
                        "label='" + label + '\'' +
                        ", ocr_text='" + ocr_text + '\'' +
                        '}';
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
}
