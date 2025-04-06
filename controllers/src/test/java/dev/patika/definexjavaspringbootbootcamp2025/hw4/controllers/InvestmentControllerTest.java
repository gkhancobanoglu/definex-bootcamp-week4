package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Investment;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.InvestmentType;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.InvestmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentControllerTest {

    @Mock
    private InvestmentService investmentService;  // Mocking the InvestmentService

    @InjectMocks
    private InvestmentController investmentController;  // Injecting mocks into the controller

    private UUID investmentId;
    private Investment investment;

    @BeforeEach
    void setUp() {
        investmentId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        InvestmentType investmentType = InvestmentType.STOCK;

        // Creating the Investment object with all required fields
        investment = new Investment(investmentId, "Test Investment", new BigDecimal("1000.00"), investmentType, now, new BigDecimal("1200.00"));

        // Manually initializing InvestmentController with mock service injected
        investmentController = new InvestmentController(investmentService); // Ensure correct injection
    }

    /**
     * Test: Tüm yatırımları listeleme
     * - Yatırım listesi boş
     */
    @Test
    void shouldReturnNoContentWhenInvestmentsAreEmpty() {
        when(investmentService.findInvestments()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Investment>> response = investmentController.getInvestments();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());  // Empty body should be null
        verify(investmentService, times(1)).findInvestments();
    }

    /**
     * Test: Tüm yatırımları listeleme
     * - Yatırım listesi varsa
     */
    @Test
    void shouldReturnInvestmentsWhenAvailable() {
        when(investmentService.findInvestments()).thenReturn(Collections.singletonList(investment));

        ResponseEntity<List<Investment>> response = investmentController.getInvestments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(investmentId, response.getBody().get(0).getId());
        verify(investmentService, times(1)).findInvestments();
    }

    /**
     * Test: Yatırım oluşturma
     * - Geçersiz yatırım miktarı
     */
    @Test
    void shouldReturnBadRequestWhenInvestmentAmountIsZeroOrNegative() {
        Investment invalidInvestment = new Investment(
                UUID.randomUUID(),
                "Invalid Investment",
                BigDecimal.ZERO,
                InvestmentType.STOCK,
                LocalDateTime.now(),
                BigDecimal.ZERO
        );

        ResponseEntity<Investment> response = investmentController.createInvestment(invalidInvestment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(investmentService, times(0)).create(invalidInvestment);
    }

    /**
     * Test: Yatırım oluşturma
     * - Geçerli yatırım
     */
    @Test
    void shouldCreateInvestment() {
        when(investmentService.create(investment)).thenReturn(investment);

        ResponseEntity<Investment> response = investmentController.createInvestment(investment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(investmentId, response.getBody().getId());
        verify(investmentService, times(1)).create(investment);
    }

    /**
     * Test: Yatırım performansını getirme
     * - Performans verisi mevcut
     */
    @Test
    void shouldReturnPerformanceMetrics() {
        Map<String, Object> performance = new HashMap<>();
        performance.put("Performance", "Good");
        when(investmentService.getPerformanceMetrics()).thenReturn(performance);

        ResponseEntity<Map<String, Object>> response = investmentController.getPerformanceMetrics();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(investmentService, times(1)).getPerformanceMetrics();
    }

    /**
     * Test: Yatırım performansını getirme
     * - Performans verisi yok
     */
    @Test
    void shouldReturnNoContentWhenPerformanceMetricsIsEmpty() {
        when(investmentService.getPerformanceMetrics()).thenReturn(Collections.emptyMap());

        ResponseEntity<Map<String, Object>> response = investmentController.getPerformanceMetrics();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(investmentService, times(1)).getPerformanceMetrics();
    }

    /**
     * Test: Yatırım portföyü özeti getirme
     * - Portföy özeti mevcut
     */
    @Test
    void shouldReturnPortfolioSummary() {
        Map<String, Object> portfolioSummary = new HashMap<>();
        portfolioSummary.put("Total", new BigDecimal("5000.00"));
        when(investmentService.getPortfolioSummary()).thenReturn(portfolioSummary);

        ResponseEntity<Map<String, Object>> response = investmentController.getPortfolioSummary();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(investmentService, times(1)).getPortfolioSummary();
    }

    /**
     * Test: Yatırım portföyü özeti getirme
     * - Portföy özeti boş
     */
    @Test
    void shouldReturnNoContentWhenPortfolioSummaryIsEmpty() {
        when(investmentService.getPortfolioSummary()).thenReturn(Collections.emptyMap());

        ResponseEntity<Map<String, Object>> response = investmentController.getPortfolioSummary();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(investmentService, times(1)).getPortfolioSummary();
    }
}
