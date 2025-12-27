package com.demo.project;

import com.demo.project.tests.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OperatiiTipDobandaTests {

    @Mock
    private UserInterfaceDB userInterfaceDB;
    private OperatiiDobanda operatiiDobanda = new OperatiiDobanda();

    @Test
    void testDobandaMica() {
        double result = operatiiDobanda.calculDobanda(TipDobanda.MICA);
        double expectedResult = 0.1;
        assertEquals(result, expectedResult);
    }

    @Test
    void testUserRiscMare() {
        User user = new User("Raluca", TipRisc.MARE);
        when(userInterfaceDB.getUser()).thenReturn(user);
        OperatiiDobanda operatiiDobanda2 = new OperatiiDobanda(userInterfaceDB);

        TipDobanda result = operatiiDobanda2.calculDobandaCuRisc();
        TipDobanda expectedResult = TipDobanda.MICA;
        assertEquals(result, expectedResult);
        verify(userInterfaceDB).getUser();
    }

    @Test
    void testUserRiscMediu() {
        User user = new User("Raluca", TipRisc.MEDIU);
        when(userInterfaceDB.getUser()).thenReturn(user);
        OperatiiDobanda operatiiDobanda2 = new OperatiiDobanda(userInterfaceDB);

        TipDobanda result = operatiiDobanda2.calculDobandaCuRisc();
        TipDobanda expectedResult = TipDobanda.MEDIE;
        assertEquals(result, expectedResult);
        verify(userInterfaceDB).getUser();
    }

    @Test
    void testUserRiscMic() {
        User user = new User("Raluca", TipRisc.MIC);
        when(userInterfaceDB.getUser()).thenReturn(user);
        OperatiiDobanda operatiiDobanda2 = new OperatiiDobanda(userInterfaceDB);

        TipDobanda result = operatiiDobanda2.calculDobandaCuRisc();
        TipDobanda expectedResult = TipDobanda.MARE;
        assertEquals(result, expectedResult);
        verify(userInterfaceDB).getUser();
    }
}
