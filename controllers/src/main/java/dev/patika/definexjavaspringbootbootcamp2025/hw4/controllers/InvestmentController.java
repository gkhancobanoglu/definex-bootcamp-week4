package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Investment;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.InvestmentService;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    //Tüm yatıtımları listeleme
    @GetMapping("/v1")
    public ResponseEntity<List<Investment>> getInvestments() {
        List<Investment> investments = investmentService.findInvestments();
        if(investments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(investments);
    }

    //Yatırım olusturma
    @PostMapping("/v1")
    public ResponseEntity<Investment> createInvestment(@RequestBody Investment investment) {
        if (investment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // 400 döndür, geçersiz yatırım miktarı
        }
        Investment createdInvestment = investmentService.create(investment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvestment);
    }

    //Yatırım performansını getirme
    @GetMapping("/v1/performance")
    public ResponseEntity<Map<String, Object>> getPerformanceMetrics() {
        Map<String, Object> performance = investmentService.getPerformanceMetrics();
        if (performance.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 döndür
        }
        return ResponseEntity.ok(performance); // 200 döndür
    }

    // Portföy özetini getirme
    @GetMapping("/v1/portfolio")
    public ResponseEntity<Map<String, Object>> getPortfolioSummary() {
        Map<String, Object> portfolioSummary = investmentService.getPortfolioSummary();
        if (portfolioSummary.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 döndür
        }
        return ResponseEntity.ok(portfolioSummary); // 200 döndür
    }
}
