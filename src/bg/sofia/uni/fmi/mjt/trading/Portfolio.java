package bg.sofia.uni.fmi.mjt.trading;

import java.time.LocalDateTime;
import java.util.Arrays;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;

public class Portfolio implements PortfolioAPI
{
    private static final int PERCENTAGE_INCREASE = 5;
    private final String owner;
    private final PriceChartAPI priceChart;
    private StockPurchase[] stockPurchases;
    private double budget;
    private int size;
    private int maxSize;

    Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize)
    {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;
        this.stockPurchases = new StockPurchase[] {};
    }

    Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize)
    {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;
        this.size = stockPurchases.length;
        this.stockPurchases = Arrays.copyOf(stockPurchases, maxSize);
    }

    @Override 
    public String getOwner()
    {
        return this.owner;
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity)
    {
        if(null == stockTicker || quantity <= 0 || size == maxSize)
        {
            return null;
        }

        return switch(stockTicker)
        {
            case MicrosoftStockPurchase.STOCK_TICKER ->
                    attemptStockPurchase(new MicrosoftStockPurchase(quantity, LocalDateTime.now(), priceChart.getCurrentPrice(stockTicker)));
            case GoogleStockPurchase.STOCK_TICKER ->
                attemptStockPurchase(new GoogleStockPurchase(quantity, LocalDateTime.now(), priceChart.getCurrentPrice(stockTicker)));
            case AmazonStockPurchase.STOCK_TICKER ->
                attemptStockPurchase(new AmazonStockPurchase(quantity, LocalDateTime.now(), priceChart.getCurrentPrice(stockTicker)));
            default -> null;
        };


    }

    private StockPurchase attemptStockPurchase(StockPurchase pendingPurchase)
    {
        double totalPendingPurchasePrice = pendingPurchase.getTotalPurchasePrice();
        if(budget < totalPendingPurchasePrice)
        {
            return null;
        }
        stockPurchases[size++] = pendingPurchase;
        budget = budget - totalPendingPurchasePrice;

        priceChart.changeStockPrice(pendingPurchase.getStockTicker(), PERCENTAGE_INCREASE);
        return pendingPurchase;
    }

    @Override
    public StockPurchase[] getAllPurchases()
    {
        return Arrays.copyOf(stockPurchases, size);
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimeStamp, LocalDateTime endTimeStamp)
    {
        int purchasesInRange = countPurchasesInTimeRange(startTimeStamp, endTimeStamp);
        StockPurchase[] newStockPurchases = new StockPurchase[purchasesInRange];
        int newStockPurchasesSize = 0;

        for(int i = 0; i < size; i++)
        {
            StockPurchase curr = stockPurchases[i];
            LocalDateTime currTime = curr.getPurchaseTimestamp();
            if(isInTimeRange(currTime, startTimeStamp, endTimeStamp))
            {
                newStockPurchases[newStockPurchasesSize++] = curr;
            }
        }
        return newStockPurchases;
    }

    private int countPurchasesInTimeRange(LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        int count = 0;
        for(int i = 0; i < size; i++)
        {
            StockPurchase curr = stockPurchases[i];
            LocalDateTime currTime = curr.getPurchaseTimestamp();
            if(isInTimeRange(currTime, startPeriod, endPeriod))
            {
                count++;
            }
        }
        return count;
    }

    private boolean isInTimeRange(LocalDateTime purchaseTime, LocalDateTime startPeriod, LocalDateTime endPeriod)
    {
        return ((purchaseTime.isEqual(startPeriod) || purchaseTime.isAfter(startPeriod)) && (purchaseTime.isBefore(endPeriod) || purchaseTime.isEqual(endPeriod)));
    }

    @Override
    public double getNetworth()
    {
        double net = 0;

        for(int i = 0; i < size; i++)
        {
            StockPurchase curr = stockPurchases[i];
            String ticker = curr.getStockTicker();
            int quantity = curr.getQuantity();
            double moneyFromCurrStock = quantity * priceChart.getCurrentPrice(ticker);

            net = net + moneyFromCurrStock;
        }
        return DoubleRounder.round(net);
    }

    @Override
    public double getRemainingBudget()
    {
        return DoubleRounder.round(budget);
    }

}
