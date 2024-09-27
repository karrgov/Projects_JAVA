package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

abstract class BaseStockPurchase implements StockPurchase {
    
    private final String ticker;
    private final int quantity;
    private final LocalDateTime purchaseTimestamp;
    private final double purchasePricePerUnit;

    protected BaseStockPurchase(String ticker, int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit)
    {
        this.ticker = ticker;
        this.quantity = quantity;
        this.purchaseTimestamp = purchaseTimestamp;
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    @Override
    public int getQuantity()
    {
        return this.quantity;
    }

    @Override
    public LocalDateTime getPurchaseTimestamp()
    {
        return purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit()
    {
        return this.purchasePricePerUnit;
    }

    @Override
    public double getTotalPurchasePrice()
    {
        double total = this.purchasePricePerUnit * this.quantity;
        return total;
    }

    @Override
    public String getStockTicker()
    {
        return this.ticker;
    }
}
