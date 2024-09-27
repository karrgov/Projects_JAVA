package bg.sofia.uni.fmi.mjt.trading;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

public interface PortfolioAPI {

    StockPurchase buyStock(String stockTicker, int quantity);

    StockPurchase[] getAllPurchases();

    StockPurchase[] getAllPurchases(LocalDateTime startTimeStamp, LocalDateTime endTimeStamp);

    double getNetworth();

    double getRemainingBudget();

    String getOwner();
}
