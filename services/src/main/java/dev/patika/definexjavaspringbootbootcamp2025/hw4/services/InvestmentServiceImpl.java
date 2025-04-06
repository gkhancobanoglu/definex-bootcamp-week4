package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Investment;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.InvestmentType;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.InvestmentRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.InvestmentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {

    private final InvestmentRepository investmentRepository;


    @Override
    public List<Investment> findInvestments() {
        return investmentRepository.findAll();
    }

    @Override
    public Investment create(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Override
    public Investment update(Investment investment) throws InvestmentNotFoundException {
        if(!investmentRepository.existsById(investment.getId())){
            throw new InvestmentNotFoundException();
        }
        return investmentRepository.save(investment);
    }

    /**
     * Tüm yatırımların performansını analiz eder.
     */
    @Override
    public Map<String, Object> getPerformanceMetrics() {
        List<Investment> investments = investmentRepository.findAll();
        Map<String, Object> performanceMetrics = new HashMap<>();

        for (Investment investment : investments) {
            BigDecimal initialAmount = investment.getAmount();
            BigDecimal currentValue = investment.getCurrentValue();

            // Yüzdesel kazanç/kayıp hesaplama
            BigDecimal profitLoss = currentValue.subtract(initialAmount);
            double percentageChange = initialAmount.compareTo(BigDecimal.ZERO) > 0
                    ? profitLoss.divide(initialAmount, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100
                    : 0.0;

            // Durum belirleme
            String status = profitLoss.compareTo(BigDecimal.ZERO) > 0 ? "Profitable" :
                    (profitLoss.compareTo(BigDecimal.ZERO) < 0 ? "Loss" : "No Change");

            // Yatırım bilgilerini ekleme
            Map<String, Object> investmentData = new HashMap<>();
            investmentData.put("initialAmount", initialAmount);
            investmentData.put("currentValue", currentValue);
            investmentData.put("profitLoss", profitLoss);
            investmentData.put("percentageChange", percentageChange);
            investmentData.put("status", status);

            performanceMetrics.put(investment.getName(), investmentData);
        }

        return performanceMetrics;
    }


    /**
     * Portföydeki yatırımları özetler.
     */
    @Override
    public Map<String, Object> getPortfolioSummary() {
        List<Investment> investments = investmentRepository.findAll();
        Map<String, Object> portfolioSummary = new HashMap<>();

        // Toplam yatırım değeri hesaplama
        BigDecimal totalInvestment = investments.stream()
                .map(Investment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCurrentValue = investments.stream()
                .map(Investment::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Portföy içindeki her yatırım türüne göre toplam yatırım miktarını gruplama
        Map<InvestmentType, BigDecimal> investmentTypeDistribution = investments.stream()
                .collect(Collectors.groupingBy(Investment::getType,
                        Collectors.reducing(BigDecimal.ZERO, Investment::getCurrentValue, BigDecimal::add)));

        // En iyi ve en kötü yatırım belirleme
        Optional<Investment> bestInvestment = investments.stream()
                .max(Comparator.comparing(inv -> inv.getCurrentValue().subtract(inv.getAmount())));

        Optional<Investment> worstInvestment = investments.stream()
                .min(Comparator.comparing(inv -> inv.getCurrentValue().subtract(inv.getAmount())));

        // Sonuçları map içine ekleme
        portfolioSummary.put("totalInvestment", totalInvestment);
        portfolioSummary.put("totalCurrentValue", totalCurrentValue);
        portfolioSummary.put("investmentTypeDistribution", investmentTypeDistribution);
        portfolioSummary.put("bestInvestment", bestInvestment.map(Investment::getName).orElse("None"));
        portfolioSummary.put("worstInvestment", worstInvestment.map(Investment::getName).orElse("None"));

        return portfolioSummary;
    }
}
