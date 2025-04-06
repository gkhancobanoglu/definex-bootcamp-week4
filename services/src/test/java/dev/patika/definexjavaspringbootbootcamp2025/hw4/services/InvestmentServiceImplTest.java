package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Investment;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.InvestmentType;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.InvestmentRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.InvestmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceImplTest {

    @Mock
    private InvestmentRepository investmentRepository;

    @InjectMocks
    private InvestmentServiceImpl investmentService;

    private UUID investmentId;
    private Investment profitableInvestment;
    private Investment lossInvestment;
    private Investment noChangeInvestment;

    @BeforeEach
    void setUp() {
        investmentId = UUID.randomUUID();

        profitableInvestment = Investment.builder()
                .id(investmentId)
                .name("Profitable Investment")
                .amount(new BigDecimal("1000.00"))
                .currentValue(new BigDecimal("1200.00"))
                .type(InvestmentType.REAL_ESTATE)
                .build();

        lossInvestment = Investment.builder()
                .id(UUID.randomUUID())
                .name("Loss Investment")
                .amount(new BigDecimal("1000.00"))
                .currentValue(new BigDecimal("800.00"))
                .type(InvestmentType.STOCK)
                .build();

        noChangeInvestment = Investment.builder()
                .id(UUID.randomUUID())
                .name("No Change Investment")
                .amount(new BigDecimal("1000.00"))
                .currentValue(new BigDecimal("1000.00"))
                .type(InvestmentType.FUND)
                .build();
    }


    /**
     * ✅ Tüm yatırımları bulma testi (findInvestments)
     */
    @Test
    void shouldFindAllInvestments() {
        when(investmentRepository.findAll()).thenReturn(List.of(profitableInvestment, lossInvestment, noChangeInvestment));

        List<Investment> result = investmentService.findInvestments();

        assertNotNull(result);
        assertEquals(3, result.size(), "Expected 3 investments.");
        verify(investmentRepository, times(1)).findAll();
    }

    /**
     * ✅ Yeni yatırım oluşturma testi (create)
     */
    @Test
    void shouldCreateInvestment() {
        when(investmentRepository.save(profitableInvestment)).thenReturn(profitableInvestment);

        Investment result = investmentService.create(profitableInvestment);

        assertNotNull(result);
        assertEquals("Profitable Investment", result.getName());
        assertEquals(new BigDecimal("1000.00"), result.getAmount());
        verify(investmentRepository, times(1)).save(profitableInvestment);
    }

    // --- Diğer Testler ---

    @Test
    void shouldGetPerformanceMetricsForProfitableInvestment() {
        when(investmentRepository.findAll()).thenReturn(List.of(profitableInvestment));

        Map<String, Object> result = investmentService.getPerformanceMetrics();

        assertNotNull(result);
        assertTrue(result.containsKey("Profitable Investment"));
        Map<String, Object> investmentData = (Map<String, Object>) result.get("Profitable Investment");
        assertEquals(new BigDecimal("1000.00"), investmentData.get("initialAmount"));
        assertEquals(new BigDecimal("1200.00"), investmentData.get("currentValue"));
        assertEquals(new BigDecimal("200.00"), investmentData.get("profitLoss"));
        assertEquals(20.0, investmentData.get("percentageChange"));
        assertEquals("Profitable", investmentData.get("status"));
        verify(investmentRepository, times(1)).findAll();
    }

    @Test
    void shouldGetPerformanceMetricsForLossInvestment() {
        when(investmentRepository.findAll()).thenReturn(List.of(lossInvestment));

        Map<String, Object> result = investmentService.getPerformanceMetrics();

        assertNotNull(result);
        assertTrue(result.containsKey("Loss Investment"));
        Map<String, Object> investmentData = (Map<String, Object>) result.get("Loss Investment");
        assertEquals(new BigDecimal("1000.00"), investmentData.get("initialAmount"));
        assertEquals(new BigDecimal("800.00"), investmentData.get("currentValue"));
        assertEquals(new BigDecimal("-200.00"), investmentData.get("profitLoss"));
        assertEquals(-20.0, investmentData.get("percentageChange"));
        assertEquals("Loss", investmentData.get("status"));
        verify(investmentRepository, times(1)).findAll();
    }

    @Test
    void shouldGetPerformanceMetricsForNoChangeInvestment() {
        when(investmentRepository.findAll()).thenReturn(List.of(noChangeInvestment));

        Map<String, Object> result = investmentService.getPerformanceMetrics();

        assertNotNull(result);
        assertTrue(result.containsKey("No Change Investment"));
        Map<String, Object> investmentData = (Map<String, Object>) result.get("No Change Investment");
        assertEquals(new BigDecimal("1000.00"), investmentData.get("initialAmount"));
        assertEquals(new BigDecimal("1000.00"), investmentData.get("currentValue"));
        assertEquals(new BigDecimal("0.00"), investmentData.get("profitLoss"));
        assertEquals(0.0, investmentData.get("percentageChange"));
        assertEquals("No Change", investmentData.get("status"));
        verify(investmentRepository, times(1)).findAll();
    }

    @Test
    void shouldGetPortfolioSummary() {
        when(investmentRepository.findAll()).thenReturn(List.of(profitableInvestment, lossInvestment, noChangeInvestment));

        Map<String, Object> result = investmentService.getPortfolioSummary();

        assertNotNull(result);
        assertEquals(new BigDecimal("3000.00"), result.get("totalInvestment"));
        assertEquals(new BigDecimal("3000.00"), result.get("totalCurrentValue"));
        assertTrue(result.containsKey("investmentTypeDistribution"));
        assertEquals("Profitable Investment", result.get("bestInvestment"));
        assertEquals("Loss Investment", result.get("worstInvestment"));
        verify(investmentRepository, times(1)).findAll();
    }

    @Test
    void shouldHandleNoBestOrWorstInvestment() {
        when(investmentRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, Object> result = investmentService.getPortfolioSummary();

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.get("totalInvestment"));
        assertEquals(BigDecimal.ZERO, result.get("totalCurrentValue"));
        assertTrue(result.containsKey("investmentTypeDistribution"));
        assertEquals("None", result.get("bestInvestment"));
        assertEquals("None", result.get("worstInvestment"));
        verify(investmentRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingInvestment() {
        when(investmentRepository.existsById(investmentId)).thenReturn(false);

        assertThrows(InvestmentNotFoundException.class, () -> investmentService.update(profitableInvestment));
        verify(investmentRepository, times(1)).existsById(investmentId);
    }

    @Test
    void shouldUpdateExistingInvestment() {
        when(investmentRepository.existsById(investmentId)).thenReturn(true);
        when(investmentRepository.save(profitableInvestment)).thenReturn(profitableInvestment);

        Investment result = investmentService.update(profitableInvestment);

        assertNotNull(result);
        assertEquals(investmentId, result.getId());
        verify(investmentRepository, times(1)).existsById(investmentId);
        verify(investmentRepository, times(1)).save(profitableInvestment);
    }

    @Test
    void shouldHandleEmptyInvestmentListInPerformanceMetrics() {
        when(investmentRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, Object> result = investmentService.getPerformanceMetrics();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result should be empty when no investments are found.");
        verify(investmentRepository, times(1)).findAll();
    }
}
