package com.example.spending.Network;

import java.util.Arrays;

public class ResponseReceiptModal {
    public ParsedResult[] ParsedResults;
    public String OCRExitCode;
    public String IsErroredOnProcessing;
    public String ProcessingTimeInMilliseconds;
    public String SearchablePDFURL;

    @Override
    public String toString() {
        return "ResponseReceiptModal{" +
                "parseResults=" + Arrays.toString(ParsedResults) +
                ", OCRExitCode='" + OCRExitCode + '\'' +
                ", IsErroredOnProcessing='" + IsErroredOnProcessing + '\'' +
                ", ProcessingTimeInMilliseconds='" + ProcessingTimeInMilliseconds + '\'' +
                ", SearchablePDFURL='" + SearchablePDFURL + '\'' +
                '}';
    }

    private static class ParsedResult {
        public String TextOrientation;
        public String FileParseExitCode;
        public String ParsedText;
        public String ErrorMessage;
        public String ErrorDetails;
        public TextOverLay TextOverlay;

        private static class TextOverLay {
            public String[] Lines;
            public Boolean HasOverlay;
            public String Message;

            @Override
            public String toString() {
                return "TextOverLay{" +
                        "lines=" + Arrays.toString(Lines) +
                        ", HasOverlay=" + HasOverlay +
                        ", Message='" + Message + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ParsedResult{" +
                    "TextOrientation='" + TextOrientation + '\'' +
                    ", FileParseExitCode='" + FileParseExitCode + '\'' +
                    ", ParsedText='" + ParsedText + '\'' +
                    ", ErrorMessage='" + ErrorMessage + '\'' +
                    ", ErrorDetails='" + ErrorDetails + '\'' +
                    ", TextOverlay=" + TextOverlay +
                    '}';
        }
    }

}
