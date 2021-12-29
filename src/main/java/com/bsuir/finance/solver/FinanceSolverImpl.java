package com.bsuir.finance.solver;

import com.bsuir.models.Deposit;
import com.bsuir.models.UserActive;
import com.bsuir.services.CurrencyService;
import com.bsuir.services.DepositService;
import com.bsuir.services.UserActiveService;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

@Component
@Slf4j
public class FinanceSolverImpl implements FinanceSolver {

    private final DepositService depositService;
    private final CurrencyService currencyService;
    private final UserActiveService userActiveService;

    @Autowired
    public FinanceSolverImpl(DepositService depositService, CurrencyService currencyService, UserActiveService userActiveService) {
        this.depositService = depositService;
        this.currencyService = currencyService;
        this.userActiveService = userActiveService;
    }

    private static final Double INFINITY = Double.POSITIVE_INFINITY;

    @Override
    public Map<String, Object> calculateInvestments(String username) {
        Loader.loadNativeLibraries();
        BigDecimal initialAmountInUsd = getStartSumInCurrency("USD", username);
        log.info("Initial amount: {}", initialAmountInUsd);
        initialAmountInUsd = initialAmountInUsd.negate();

        MPSolver solver = MPSolver.createSolver("GLOP");

        Map<Integer, CurrencyElem> currencyElemMap = getCurrencyElemMap(solver, username);
        log.info("CurrencyElemMap: {}", currencyElemMap);

        MPObjective objective = solver.objective();
        initialAmountInUsd = fillObjective(depositService.findAll(), currencyElemMap, solver, initialAmountInUsd, username);

        currencyElemMap.values().forEach(elem -> objective.setCoefficient(elem.getMpVariable(), elem.getCoefficient()));

        final MPVariable usd1 = solver.makeNumVar(initialAmountInUsd.doubleValue(), initialAmountInUsd.doubleValue(), "initialAmountInUsdMinusAnotherAmount");
        objective.setCoefficient(usd1, 1);
        objective.setMaximization();

        MPSolver.ResultStatus solve = solver.solve();
        String model = solver.exportModelAsLpFormat();

        Map<String, Object> response = new HashMap<>();
        response.put("Model", model);
        response.put("Func", objective.value());
        response.put("variables", Arrays.stream(solver.variables()).map(MPVariable::solutionValue).collect(Collectors.toList()));
        return response;
    }

    private BigDecimal getStartSumInCurrency(String currency, String username) {
        return userActiveService.getActiveListByUsername(username)
                .stream()
                .map(active -> {
                    Double rate = currencyService.findByCurrency(active.getCurrency()).getRates().get(currency);
                    BigDecimal decimalRate = BigDecimal.valueOf(rate == null ? 1 : rate);
                    return BigDecimal.valueOf(active.getAmount()).multiply(decimalRate);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Integer, CurrencyElem> getCurrencyElemMap(MPSolver solver, String username) {
        Map<Integer, CurrencyElem> currencyElemHashMap = new HashMap<>();
        int number = 0;

        for (UserActive active : userActiveService.getActiveListByUsername(username)) {
            BigDecimal amount = BigDecimal.valueOf(active.getAmount());
            String defaultCurrency = active.getCurrency();
            Map<String, Double> nowCurses = currencyService.findByCurrency(defaultCurrency).getRates();

            for (Map.Entry<String, Double> entry : nowCurses.entrySet()) {
                MPVariable variable = solver.makeNumVar(0, INFINITY, "x" + number);
                currencyElemHashMap.put(number,
                        new CurrencyElem(amount, defaultCurrency, entry.getKey(), BigDecimal.valueOf(entry.getValue()), variable, 0));
                number++;
            }

        }
        return currencyElemHashMap;
    }

    private BigDecimal fillObjective(List<Deposit> deposits, Map<Integer, CurrencyElem> currencyElemMap, MPSolver solver, BigDecimal initialAmountInUsd, String username) {
        deposits = deposits.stream()
                .filter(deposit -> {
                    List<String> collect = userActiveService.getActiveListByUsername(username).stream().map(UserActive::getCurrency).collect(Collectors.toList());
                    return collect.contains(deposit.getCurrency());
                })
                .collect(Collectors.toList());
        for (Deposit deposit : deposits) {
            final double V = calcV(deposit.getDuration(), 0, deposit.getPercent() / 100, 0.13);
            String currency = deposit.getCurrency();
            List<CurrencyElem> currenciesFrom = getCurrencyElemsByCurrencyAndFromTo(currencyElemMap, deposit.getCurrency(), true);
            List<CurrencyElem> currenciesTo = getCurrencyElemsByCurrencyAndFromTo(currencyElemMap, deposit.getCurrency(), false);
            UserActive active = userActiveService.getActiveListByUsername(username).stream()
                    .filter(elem -> elem.getCurrency().equals(deposit.getCurrency()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some error while calculating user active"));
            double startAmount = active.getAmount();
            initialAmountInUsd = initialAmountInUsd.add(BigDecimal.valueOf(startAmount * V * getFutureRate(currency, "USD").doubleValue()));
            currenciesTo.forEach(currencyElem -> currencyElem.setCoefficient(currencyElem.getCoefficient() + ((V * currencyElem.getCurs().doubleValue()) * getFutureRate(currencyElem.getTo(), "USD").doubleValue())));
            MPConstraint constraint = solver.makeConstraint(-INFINITY, startAmount, "Constraint");
            currenciesFrom.forEach(currencyElem -> {
                        currencyElem.setCoefficient(currencyElem.getCoefficient() - (V * getFutureRate(currencyElem.getFrom(), "USD").doubleValue()));
                        constraint.setCoefficient(currencyElem.getMpVariable(), 1);
                    }
            );
        }
        return initialAmountInUsd;
    }

    static double calcV(int n, int m, double i, double tax) {
        return pow((1 + i), n) - tax * (pow((1 + i), n) - 1);
    }

    private static List<CurrencyElem> getCurrencyElemsByCurrencyAndFromTo(Map<Integer, CurrencyElem> currencyElemMap, String currency, boolean isFrom) {
        return currencyElemMap.values().stream()
                .filter(elem -> currency.equals(isFrom ? elem.getFrom() : elem.getTo()))
                .collect(Collectors.toList());
    }

    BigDecimal getFutureRate(String from, String to) {
        double accretion = 1 + ((double) (new Random().nextInt(10) + 1) / 100);
        Double rate = currencyService.findByCurrency(from).getRates().get(to);
        return BigDecimal.valueOf(rate == null ? 1 : rate * accretion);
    }

    @Data
    @AllArgsConstructor
    static class CurrencyElem {

        private BigDecimal amount;
        private String from;
        private String to;
        private BigDecimal curs;
        private MPVariable mpVariable;
        double coefficient;
    }
}

