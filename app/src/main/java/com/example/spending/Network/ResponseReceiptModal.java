package com.example.spending.Network;

import java.util.ArrayList;
import java.util.Arrays;

public class ResponseReceiptModal {
    private ArrayList<ParsedResult> ParsedResults;
    private String OCRExitCode;
    private String IsErroredOnProcessing;
    private String ProcessingTimeInMilliseconds;
    private String SearchablePDFURL;

    @Override
    public String toString() {
        return "ResponseReceiptModal{" +
                "parseResults=" + Arrays.toString(new ArrayList[]{ParsedResults}) +
                ", OCRExitCode='" + OCRExitCode + '\'' +
                ", IsErroredOnProcessing='" + IsErroredOnProcessing + '\'' +
                ", ProcessingTimeInMilliseconds='" + ProcessingTimeInMilliseconds + '\'' +
                ", SearchablePDFURL='" + SearchablePDFURL + '\'' +
                '}';
    }

    public ArrayList<ParsedResult> getParsedResults() {
        return ParsedResults;
    }

    public String getOCRExitCode() {
        return OCRExitCode;
    }

    public String getIsErroredOnProcessing() {
        return IsErroredOnProcessing;
    }

    public String getProcessingTimeInMilliseconds() {
        return ProcessingTimeInMilliseconds;
    }

    public String getSearchablePDFURL() {
        return SearchablePDFURL;
    }

    public class ParsedResult {
        private String TextOrientation;
        private String FileParseExitCode;
        private String ParsedText;
        private String ErrorMessage;
        private String ErrorDetails;
        private TextOverLay TextOverlay;

        public String getTextOrientation() {
            return TextOrientation;
        }

        public String getFileParseExitCode() {
            return FileParseExitCode;
        }

        public String getParsedText() {
            return ParsedText;
        }

        public String getErrorMessage() {
            return ErrorMessage;
        }

        public String getErrorDetails() {
            return ErrorDetails;
        }

        public TextOverLay getTextOverlay() {
            return TextOverlay;
        }

        public class TextOverLay {
            private String[] Lines;
            private Boolean HasOverlay;
            private String Message;

            @Override
            public String toString() {
                return "TextOverLay{" +
                        "lines=" + Arrays.toString(Lines) +
                        ", HasOverlay=" + HasOverlay +
                        ", Message='" + Message + '\'' +
                        '}';
            }

            public String[] getLines() {
                return Lines;
            }

            public Boolean getHasOverlay() {
                return HasOverlay;
            }

            public String getMessage() {
                return Message;
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
