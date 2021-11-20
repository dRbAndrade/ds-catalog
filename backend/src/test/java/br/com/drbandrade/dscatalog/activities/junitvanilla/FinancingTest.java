package br.com.drbandrade.dscatalog.activities.junitvanilla;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FinancingTest {

    Financing f;
    @BeforeEach
    public void initiate(){
        f = Financing.generateFinancing(100000,2000,80);
    }
    @Test
    public void factoryMethodShouldCreateObjectWhenValidArguments(){

        assertNotNull(f);
        assertEquals(100000.0,f.getTotalAmount());
        assertEquals(2000.0,f.getIncome());
        assertEquals(80,f.getMonths());

    }
    @Test
    public void factoryMethodShouldThrowExceptionWhenInvalidArguments(){

        assertThrows(IllegalArgumentException.class,()->Financing.generateFinancing(100000,2000,20));

    }

    @Test
    public void setTotalAmountShouldUpdateValueWhenValidArguments(){

        f.setTotalAmount(80000);
        assertEquals(80000,f.getTotalAmount());

    }

    @Test
    public void setTotalAmountShouldThrowExceptionWhenInvalidArguments(){

        assertThrows(IllegalArgumentException.class,()->f.setTotalAmount(999999999));
        assertThrows(IllegalArgumentException.class,()->f.setTotalAmount(-200));

    }

    @Test
    public void setIncomeShouldUpdateValueWhenValidArguments(){

        f.setIncome(4000);
        assertEquals(4000,f.getIncome());

    }

    @Test
    public void setIncomeShouldThrowExceptionWhenInvalidArguments(){

        assertThrows(IllegalArgumentException.class,()->f.setIncome(-100));
        assertThrows(IllegalArgumentException.class,()->f.setIncome(500));

    }

    @Test
    public void setMonthsShouldUpdateValueWhenValidArguments(){

        f.setMonths(100);
        assertEquals(100,f.getMonths());

    }

    @Test
    public void setMonthsShouldThrowExceptionWhenInvalidArguments(){

        assertThrows(IllegalArgumentException.class,()->f.setMonths(-1));
        assertThrows(IllegalArgumentException.class,()->f.setMonths(1));


    }

    @Test
    public void entryShouldCalculateEntryValue(){
        assertEquals(20000.0,f.entry());
    }

    @Test
    public void quotaShouldCalculateQuotaValue(){
        assertEquals(1000,f.quota());
    }


}
