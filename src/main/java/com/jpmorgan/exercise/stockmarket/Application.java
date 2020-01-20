package com.jpmorgan.exercise.stockmarket;

import com.jpmorgan.exercise.stockmarket.dao.impl.InMemoryTradeDaoImpl;
import com.jpmorgan.exercise.stockmarket.exception.InvalidDataException;
import com.jpmorgan.exercise.stockmarket.exception.InvalidDividendException;
import com.jpmorgan.exercise.stockmarket.exception.InvalidPriceException;
import com.jpmorgan.exercise.stockmarket.model.*;
import com.jpmorgan.exercise.stockmarket.service.StockService;
import com.jpmorgan.exercise.stockmarket.service.TradeService;
import com.jpmorgan.exercise.stockmarket.service.impl.StockServiceImpl;
import com.jpmorgan.exercise.stockmarket.service.impl.TradeServiceImpl;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

    private Scanner scanner = null;
    private Map<String, Stock> stockMap = new ConcurrentHashMap<>();
    private StockService stockService = null;
    private TradeService tradeService = null;

    private Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        Application application = new Application();
        InMemoryTradeDaoImpl inMemoryTradeDao = new InMemoryTradeDaoImpl();
        StockServiceImpl stockService = new StockServiceImpl();
        TradeServiceImpl tradeService = new TradeServiceImpl();
        tradeService.setTradeDao(inMemoryTradeDao);
        stockService.setTradeService(tradeService);
        application.setStockService(stockService);
        application.setTradeService(tradeService);
        application.run();
    }

    private void run() {
        logger.info("Running Super Simple Stock Market.");
        loadStocks();
        printMenu();

        scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        while (!"0".equals(choice)) {
            try {
                int option = Integer.parseInt(choice);
                Stock stockFromUser;
                BigDecimal priceFromUser;

                switch (option) {
                    case 0:
                        logger.info("Exit Application.");
                        System.exit(0);
                        break;
                    case 1:
                        stockFromUser = getStockSymbolFromUser();
                        priceFromUser = getStockPriceFromUser();
                        calculateDividendYield(stockFromUser, priceFromUser);
                        break;
                    case 2:
                        stockFromUser = getStockSymbolFromUser();
                        priceFromUser = getStockPriceFromUser();
                        calculatePERatio(stockFromUser, priceFromUser);
                        break;
                    case 3:
                        stockFromUser = getStockSymbolFromUser();
                        BigInteger quantityFromUser = getQuantityFromUser();
                        BuySellIndicator buySellIndicator = getBuySellIndicator();
                        priceFromUser = getStockPriceFromUser();
                        recordNewTrade(stockFromUser, quantityFromUser, buySellIndicator, priceFromUser);
                        break;
                    case 4:
                        showAllTrades();
                        break;
                    case 5:
                        stockFromUser = getStockSymbolFromUser();
                        calculateVolumeWeightedStockPrice(stockFromUser);
                        break;
                    case 6:
                        calculateGBCEAllShareIndex();
                        break;
                    default:
                        break;
                }
            } catch (NumberFormatException e) {
                logger.info("Invalid Option. Choice : " + choice);
            } catch (InvalidDataException | InvalidPriceException ide) {
                logger.info("Invalid Data Entered. " + ide.getMessage());
            } catch (InvalidDividendException ex) {
                logger.info("Invalid Dividend : Invalid Data Entered. " + ex.getMessage());
            }

            logger.info("--------- Choices for Action -----------");
            printMenu();
            choice = scanner.nextLine();
        }
    }

    private void showAllTrades() {
        tradeService.getAllRecordedTrades().forEach(trade -> logger.info(" Trade : " + trade));
    }

    private void calculateGBCEAllShareIndex() throws InvalidDataException {
        BigDecimal result = stockService.calculateGBCEAllShareIndex();
        logger.info("GBCE All Share Index : " + result);
    }

    private void recordNewTrade(Stock stockFromUser, BigInteger quantityFromUser, BuySellIndicator buySellIndicator, BigDecimal priceFromUser) throws InvalidDataException {
        Trade trade;
        if (BuySellIndicator.BUY.equals(buySellIndicator)) {
            trade = stockService.buyStock(stockFromUser, quantityFromUser, priceFromUser);
        } else {
            trade = stockService.sellStock(stockFromUser, quantityFromUser, priceFromUser);
        }
        logger.info("New Trade recorded - Trade :  " + trade);

    }

    private void calculateVolumeWeightedStockPrice(Stock stockFromUser) throws InvalidDataException {
        BigDecimal result = stockService.calculateVolumeWeightedStockPrice(stockFromUser.getStockSymbol(), 15);
        logger.info("Volume Weighted StockPrice : " + result);
    }

    private void calculatePERatio(Stock stockFromUser, BigDecimal priceFromUser) throws InvalidPriceException, InvalidDividendException {
        stockFromUser.setPrice(priceFromUser);
        BigDecimal result = stockFromUser.calculatePERatio();
        logger.info("PE Ratio : " + result);
    }

    private void calculateDividendYield(Stock stockFromUser, BigDecimal priceFromUser) throws InvalidPriceException {
        stockFromUser.setPrice(priceFromUser);
        BigDecimal result = stockFromUser.calculateDividendYield();
        logger.info("Dividend Yield : " + result);
    }

    private void loadStocks() {
        stockMap.put("TEA", CommonStock.commonStockBuilder().stockSymbol("TEA").parValue(new BigDecimal(100))
                .lastDividend(BigDecimal.ZERO).build());
        stockMap.put("POP", CommonStock.commonStockBuilder().stockSymbol("POP").parValue(new BigDecimal(100))
                .lastDividend(new BigDecimal(8)).build());
        stockMap.put("ALE", CommonStock.commonStockBuilder().stockSymbol("ALE").parValue(new BigDecimal(60))
                .lastDividend(new BigDecimal(23)).build());
        stockMap.put("GIN", PreferredStock.preferredStockBuilder().stockSymbol("GIN").parValue(new BigDecimal(100))
                .lastDividend(new BigDecimal(8)).fixedDividend(new BigDecimal(2)).build());
        stockMap.put("JOE", CommonStock.commonStockBuilder().stockSymbol("JOE").parValue(new BigDecimal(250))
                .lastDividend(new BigDecimal(13)).build());
    }

    private Stock getStockSymbolFromUser() throws InvalidDataException {
        logger.info("Please Provide Stock Symbol.");
        String stockSymbol = scanner.nextLine();
        Stock stock = stockMap.get(stockSymbol);
        if (null == stock) {
            throw new InvalidDataException("Stock Symbol not found");
        }
        return stock;
    }

    private BigDecimal getStockPriceFromUser() throws InvalidDataException {
        logger.info("Please Provide Stock Price");
        String stockPrice = scanner.nextLine();
        try {
            BigDecimal price = new BigDecimal(stockPrice);
            if (BigDecimal.ZERO.compareTo(price) >= 0) {
                throw new InvalidDataException("Error : Negative or Zero Price provided. Please provide Valid Price.");
            }
            return price;
        } catch (RuntimeException e) {
            throw new InvalidDataException("Error : Wrong Stock Price provided. Please provide Valid Price.");
        }
    }

    private BuySellIndicator getBuySellIndicator() throws InvalidDataException {
        logger.info("Please Provide BUY SELL Indicator Value (BUY/SELL)");
        String type = scanner.nextLine();
        try {
            return BuySellIndicator.valueOf(type.toUpperCase());
        } catch (RuntimeException e) {
            throw new InvalidDataException("Error : Wrong Buy Sell indicator provided. Please provide Valid data ( BUY/SELL).");
        }
    }

    private BigInteger getQuantityFromUser() throws InvalidDataException {
        logger.info("Please Provide Quantity");
        String quantity = scanner.nextLine();
        try {
            BigInteger result = new BigInteger(quantity);
            if (BigInteger.ZERO.compareTo(result) >= 0) {
                throw new InvalidDataException("Error : Negative or zero quantity provided. Please provide Valid quantity.");
            }
            return result;
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Error : Please provide Valid quantity.");
        }
    }

    private void printMenu() {
        logger.info("0: Quit");
        logger.info("1: Calculate dividend yield for stock");
        logger.info("2: Calculate P/E ratio for stock");
        logger.info("3: Record a trade for stock");
        logger.info("4: Show all trade for stock");
        logger.info("5: Calculate Volume Weighted Stock Price for stock");
        logger.info("6: Calculate GBCE All Share Index");

    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }
}



