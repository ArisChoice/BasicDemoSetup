package com.app.barber.models.request;

/**
 * Created by harish on 27/12/18.
 */

public class BookPaymentRequestModel {
    public String getStripeToken() {
        return StripeToken;
    }

    public void setStripeToken(String stripeToken) {
        StripeToken = stripeToken;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    String StripeToken;
    int Amount;

    public boolean isAction() {
        return Action;
    }

    public void setAction(boolean action) {
        Action = action;
    }

    boolean Action;


}
