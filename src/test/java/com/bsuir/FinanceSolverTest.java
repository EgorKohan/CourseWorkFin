//package com.bsuir;
//
//import com.bsuir.models.Currency;
//import com.bsuir.models.*;
//import com.bsuir.repositories.CurrencyRepository;
//import com.bsuir.repositories.DepositRepository;
//import com.bsuir.repositories.UserRepository;
//import com.bsuir.services.CurrencyService;
//import com.bsuir.services.DepositService;
//import com.bsuir.services.UserActiveService;
//import com.google.ortools.Loader;
//import com.google.ortools.linearsolver.MPConstraint;
//import com.google.ortools.linearsolver.MPObjective;
//import com.google.ortools.linearsolver.MPSolver;
//import com.google.ortools.linearsolver.MPVariable;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.types.ObjectId;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.PostConstruct;
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static java.lang.Math.pow;
//import static java.util.Objects.isNull;
//import static org.apache.commons.collections4.SetUtils.emptyIfNull;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Slf4j
//public class FinanceSolverTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private DepositRepository depositRepository;
//
//    @Autowired
//    private CurrencyRepository currencyRepository;
//
//    @Autowired
//    private UserActiveService userActiveService;
//
//    @Autowired
//    private DepositService depositService;
//
//    @Autowired
//    private CurrencyService currencyService;
//
//    public Set<UserActive> getActiveListByUsername(String username) {
//        return emptyIfNull(userRepository.findFirstByUsername(username).get().getUserActives());
//    }
//
//    private static final Double infinity = Double.POSITIVE_INFINITY;
//
//    public Set<UserActive> addActivesToUser(String username, List<UserActive> userActiveList) {
//        User user = userRepository.findFirstByUsername(username).orElse(null);
//        Set<UserActive> userActives = user.getUserActives();
//        if (isNull(userActives)) {
//            Set<UserActive> newActives = new HashSet<>();
//            newActives.addAll(userActiveList);
//            user.setUserActives(newActives);
//        } else {
//            userActives.addAll(userActiveList);
//        }
//        return userRepository.save(user).getUserActives();
//    }
//
//    @PostConstruct
//    public void init() {
//        userRepository.save(new User(new ObjectId("1"),
//                        "TestUser",
//                        "123123",
//                        "123@gmail.com",
//                        "Test",
//                        "Test",
//                        Arrays.asList(Role.USER, Role.ADMIN),
//                        Status.ACTIVE,
//                        new HashSet<>()
//                )
//        );
//        addActivesToUser("TestUser", getUserActives());
//        depositRepository.deleteAll();
//        depositRepository.saveAll(getDeposits());
//        currencyRepository.deleteAll();
//        currencyRepository.saveAll(getCertainCurrencies());
//    }
//
//    public List<UserActive> getUserActives() {
//        List<UserActive> userActives = new ArrayList<>();
//        userActives.add(new UserActive(new ObjectId("1"), 25000, "BYN"));
//        userActives.add(new UserActive(new ObjectId("2"), 11000, "USD"));
//        userActives.add(new UserActive(new ObjectId("3"), 1000, "EUR"));
//        return userActives;
//    }
//
//    private List<Currency> getCertainCurrencies() {
//        List<Currency> currencies = new ArrayList<>();
//        Map<String, Double> BYNRates = new HashMap<>();
//        BYNRates.put("USD", 1 / 2.51);
//        BYNRates.put("EUR", 1 / 2.84);
//        currencies.add(new Currency("BYN", BYNRates));
//        Map<String, Double> forUSDNow = new HashMap<>();
//        forUSDNow.put("BYN", 2.51);
//        forUSDNow.put("EUR", 1 / 1.17);
//        currencies.add(new Currency("USD", forUSDNow));
//        Map<String, Double> forEURNow = new HashMap<>();
//        forEURNow.put("BYN", 2.89);
//        forEURNow.put("USD", 1.17);
//        currencies.add(new Currency("EUR", forEURNow));
//        return currencies;
//    }
//
//    private static void calculateFutureCurrencies(List<Currency> currencies) {
//        Map<String, Double> BYNRates = new HashMap<>();
//        BYNRates.put("USD", 1 / 2.82);
//        BYNRates.put("EUR", 1 / 2.99);
//        currencies.add(new Currency("BYN", BYNRates));
//        Map<String, Double> forUSDNow = new HashMap<>();
//        forUSDNow.put("BYN", 2.82);
//        forUSDNow.put("EUR", 1 / 1.28);
//        currencies.add(new Currency("USD", forUSDNow));
//        Map<String, Double> forEURNow = new HashMap<>();
//        forEURNow.put("BYN", 2.99);
//        forEURNow.put("USD", 1.28);
//        currencies.add(new Currency("EUR", forEURNow));
//    }
//
//    private List<Deposit> getDeposits() {
//        List<Deposit> deposits = new ArrayList<>();
//        deposits.add(new Deposit(1L, "A", "", "BYN", 1, 0.09));
//        deposits.add(new Deposit(2L, "B", "", "USD", 1, 0.038));
//        deposits.add(new Deposit(3L, "C", "", "EUR", 1, 0.036));
//        return deposits;
//    }
//
//    @Test
//    public void financeTest() {
//
//        Loader.loadNativeLibraries();
//        BigDecimal initialAmountInUsd = getStartSumInCurrency("USD");
//        log.info("Initial amount: {}", initialAmountInUsd);
//        initialAmountInUsd = initialAmountInUsd.negate();
//
//        MPSolver solver = MPSolver.createSolver("GLOP");
//
//        Map<Integer, CurrencyElem> currencyElemMap = getCurrencyElemMap(solver);
//        log.info("CurrencyElemMap: {}", currencyElemMap);
//
//        MPObjective objective = solver.objective();
//        initialAmountInUsd = fillObjective(depositService.findAll(), currencyElemMap, solver, initialAmountInUsd);
//
//        currencyElemMap.values().forEach(elem -> {
//            objective.setCoefficient(elem.getMpVariable(), elem.getCoefficient());
//        });
//
//        final MPVariable usd1 = solver.makeNumVar(initialAmountInUsd.doubleValue(), initialAmountInUsd.doubleValue(), "initialAmountInUsdMinusAnotherAmount");
////        FINAL_OBJECTIVE.append(" + ").append(initialAmountInUsd.doubleValue()).append("*").append(1);
//        objective.setCoefficient(usd1, 1);
//        objective.setMaximization();
//
//        MPSolver.ResultStatus solve = solver.solve();
//        String model = solver.exportModelAsLpFormat(); // !!!!!!!!!!
//
//        log.info("Solve: {}", solve);
//        log.info("Model: {}", model);
//        log.info("Func: {}", objective.value());
//
//        final MPVariable[] variables = solver.variables();
//
//        for (MPVariable variable : variables) {
//            log.info("Variable: {} - {}", variable.name(), variable.solutionValue());
//        }
//
//    }
//
//    private BigDecimal getStartSumInCurrency(String currency) {
//        return userActiveService.getActiveListByUsername("TestUser")
//                .stream()
//                .map(active -> {
//                    Double rate = currencyService.findByCurrency(active.getCurrency()).getRates().get(currency);
//                    BigDecimal decimalRate = BigDecimal.valueOf(rate == null ? 1 : rate);
//                    return BigDecimal.valueOf(active.getAmount()).multiply(decimalRate);
//                })
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//    private Map<Integer, CurrencyElem> getCurrencyElemMap(MPSolver solver) {
//        Map<Integer, CurrencyElem> currencyElemHashMap = new HashMap<>();
//        int number = 0;
//
//        for (UserActive active : userActiveService.getActiveListByUsername("TestUser")) {
//            BigDecimal amount = BigDecimal.valueOf(active.getAmount());
//            String defaultCurrency = active.getCurrency();
//            Map<String, Double> nowCurses = currencyService.findByCurrency(defaultCurrency).getRates();
//
//            for (Map.Entry<String, Double> entry : nowCurses.entrySet()) {
//                MPVariable variable = solver.makeNumVar(0, infinity, "x" + number);
//                currencyElemHashMap.put(number,
//                        new CurrencyElem(amount, defaultCurrency, entry.getKey(), BigDecimal.valueOf(entry.getValue()), variable, 0));
//                number++;
//            }
//
//        }
//        return currencyElemHashMap;
//    }
//
//    private BigDecimal fillObjective(List<Deposit> deposits, Map<Integer, CurrencyElem> currencyElemMap, MPSolver solver, BigDecimal initialAmountInUsd) {
//        for (Deposit deposit : deposits) {
//            final double V = calcV(deposit.getDuration(), 0, deposit.getPercent(), 0.13);
//            log.info("V({}) = {}", deposit.getCurrency(), V);
//            String currency = deposit.getCurrency();
//            List<CurrencyElem> currenciesFrom = getCurrencyElemsByCurrencyAndFromTo(currencyElemMap, deposit.getCurrency(), true);
//            List<CurrencyElem> currenciesTo = getCurrencyElemsByCurrencyAndFromTo(currencyElemMap, deposit.getCurrency(), false);
//            UserActive active = userActiveService.getActiveListByUsername("TestUser").stream()
//                    .filter(elem -> elem.getCurrency().equals(deposit.getCurrency()))
//                    .findFirst()
//                    .get();
//            double startAmount = active.getAmount();
//            initialAmountInUsd = initialAmountInUsd.add(BigDecimal.valueOf(startAmount * V * getFutureRate(currency, "USD").doubleValue()));
////            FINAL_OBJECTIVE.append("( + ").append(startAmount).append("*").append("V / $'");
//            currenciesTo.forEach(currencyElem -> {
//                currencyElem.setCoefficient(currencyElem.getCoefficient() + ((V * currencyElem.getCurs().doubleValue()) * getFutureRate(currencyElem.getTo(), "USD").doubleValue()));
////                FINAL_OBJECTIVE.append(" + ").append(currencyElem.getMpVariable().name()).append("*").append("V * curs/ futureUsdCurs ").append("(" + V + " * " + currencyElem.getCurs().doubleValue() + " / " + getFutureRate(currencyElem.getTo(), "USD").doubleValue() + " ) ");
//            });
//            MPConstraint constraint = solver.makeConstraint(-infinity, startAmount, "Constraint");
//            currenciesFrom.forEach(currencyElem -> {
//                        currencyElem.setCoefficient(currencyElem.getCoefficient() - (V * getFutureRate(currencyElem.getFrom(), "USD").doubleValue()));
////                        FINAL_OBJECTIVE.append(" - ").append(currencyElem.getMpVariable().name()).append("*").append("V / futureUsdCurs");
//                        constraint.setCoefficient(currencyElem.getMpVariable(), 1);
//                    }
//            );
////            FINAL_OBJECTIVE.append(") ");
//        }
//        return initialAmountInUsd;
//    }
//
//    static double calcV(int n, int m, double i, double tax) {
//        return pow((1 + i), n) - tax * (pow((1 + i), n) - 1);
//    }
//
//    private static List<CurrencyElem> getCurrencyElemsByCurrencyAndFromTo(Map<Integer, CurrencyElem> currencyElemMap, String currency, boolean isFrom) {
//        return currencyElemMap.values().stream()
//                .filter(elem -> currency.equals(isFrom ? elem.getFrom() : elem.getTo()))
//                .collect(Collectors.toList());
//    }
//
//    BigDecimal getFutureRate(String from, String to) {
//        double accretion = 1 + ((double)(new Random().nextInt(10) + 1) / 100);
//        Double rate = currencyService.findByCurrency(from).getRates().get(to);
//        return BigDecimal.valueOf(rate == null ? 1 : rate * accretion);
//    }
//
//    @Data
//    @AllArgsConstructor
//    static class CurrencyElem {
//
//        private BigDecimal amount;
//        private String from;
//        private String to;
//        private BigDecimal curs;
//        private MPVariable mpVariable;
//        double coefficient;
//    }
//
//}
