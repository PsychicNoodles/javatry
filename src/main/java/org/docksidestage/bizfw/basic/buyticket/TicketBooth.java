/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.bizfw.basic.buyticket;

/**
 * @author jflute
 */
public class TicketBooth {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final int MAX_QUANTITY = 10;
    private static final int ONE_DAY_PRICE = 7400; // when 2019/06/15
    private static final int TWO_DAY_PRICE = 13200;
    private static final int FOUR_DAY_PRICE = 22400;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private int quantity = MAX_QUANTITY;
    private Integer salesProceeds;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public TicketBooth() {
    }

    // ===================================================================================
    //                                                                          Buy Ticket
    //                                                                          ==========
    public TicketBuyResult buyOneDayPassport(int handedMoney) {
        processPassport(handedMoney, PassportVariants.ONE_DAY);
        return new TicketBuyResult(new OneDayTicket(ONE_DAY_PRICE), handedMoney - ONE_DAY_PRICE);
    }

    public TicketBuyResult buyMultiDayPassport(int handedMoney) {
        int days = processPassport(handedMoney, PassportVariants.MULTI_DAY);
        int price = -1;
        if (days == 2) {
            price = TWO_DAY_PRICE;
        } else if (days == 4) {
            price = FOUR_DAY_PRICE;
        } else {
            throw new ArithmeticException("???");
        }
        return new TicketBuyResult(new MultiDayTicket(price, days), handedMoney - price);
    }

    private int processPassport(int handedMoney, PassportVariants variant) {
        checkQuantity();
        int days = checkHandedMoney(handedMoney, variant);
        subtractQuantity();
        addProceeds(handedMoney);
        return days;
    }

    private void checkQuantity() throws TicketSoldOutException {
        if (quantity <= 0) {
            throw new TicketSoldOutException("Sold out");
        }
    }

    private int checkHandedMoney(int handedMoney, PassportVariants variant) {
        int days;
        if (variant == PassportVariants.MULTI_DAY) {
            if (handedMoney >= FOUR_DAY_PRICE) {
                days = 4;
            } else if (handedMoney >= TWO_DAY_PRICE) {
                days = 2;
            } else {
                throw new TicketShortMoneyException("Short money for multi day: " + handedMoney);
            }
        } else {
            if (handedMoney >= ONE_DAY_PRICE) {
                days = 1;
            } else {
                throw new TicketShortMoneyException("Short money for single day: " + handedMoney);
            }
        }
        return days;
    }

    private void subtractQuantity() {
        --quantity;
    }

    private void addProceeds(int handedMoney) {
        if (salesProceeds != null) {
            salesProceeds += handedMoney;
        } else {
            salesProceeds = handedMoney;
        }
    }

    private enum PassportVariants {
        ONE_DAY, MULTI_DAY;
    }

    public static class TicketSoldOutException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TicketSoldOutException(String msg) {
            super(msg);
        }
    }

    public static class TicketShortMoneyException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TicketShortMoneyException(String msg) {
            super(msg);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public int getQuantity() {
        return quantity;
    }

    public Integer getSalesProceeds() {
        return salesProceeds;
    }

    public static int getOneDayPrice() {
        return ONE_DAY_PRICE;
    }

    public static int getTwoDayPrice() {
        return TWO_DAY_PRICE;
    }

    public static int getFourDayPrice() {
        return FOUR_DAY_PRICE;
    }
}
