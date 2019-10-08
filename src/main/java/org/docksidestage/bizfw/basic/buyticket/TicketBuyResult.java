package org.docksidestage.bizfw.basic.buyticket;

public class TicketBuyResult {
    private final Ticket ticket;
    private final int change;

    public TicketBuyResult(Ticket tick, int ch) { ticket = tick; change = ch; }

    public Ticket getTicket() {
        return ticket;
    }
    public int getChange() {
        return change;
    }
}
