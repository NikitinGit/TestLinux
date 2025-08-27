package com.example.testlinux.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {
    @JsonProperty("id")
    private String id;
    private Amount amount;
    private String status;
    private Confirmation confirmation;

    public String getId(){
        return id;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public static class Amount {
        @JsonProperty("value")
        private String value;

        public String getValue() {
            return value;
        }

    }

    public static class Confirmation {
        @JsonProperty("type")
        private String type;
        @JsonProperty("confirmation_url")
        private String confirmationUrl;

        public String getType() {
            return type;
        }
        public String getConfirmationUrl() {
            return confirmationUrl;
        }
    }
}
