import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Utility class for salary calculation
class SalaryCalculator {
    public static double calculateYearlySalary(double baseSalary, double bonus) {
        return (baseSalary + bonus) * 12;
    }
}

// JUnit test cases
public class SalaryTest {

    @Test
    public void testStandardSalary() {
        double result = SalaryCalculator.calculateYearlySalary(6000.0, 500.0);
        assertEquals(78000.0, result, 0.01);
    }

    @Test
    public void testZeroBonus() {
        double result = SalaryCalculator.calculateYearlySalary(5500.0, 0.0);
        assertEquals(66000.0, result, 0.01);
    }

    @Test
    public void testZeroSalary() {
        double result = SalaryCalculator.calculateYearlySalary(0.0, 1000.0);
        assertEquals(12000.0, result, 0.01);
    }

    @Test
    public void testNegativeBonus() {
        double result = SalaryCalculator.calculateYearlySalary(8000.0, -500.0);
        assertEquals(90000.0, result, 0.01);
    }

    @Test
    public void testLargeValues() {
        double result = SalaryCalculator.calculateYearlySalary(10000.0, 5000.0);
        assertEquals(180000.0, result, 0.01);
    }
}
