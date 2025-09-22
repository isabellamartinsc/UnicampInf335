package br.unicamp.ic.inf335.beans;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoBeanTest {

    @Test
    void compareTo_maior_menor_igual() {
        ProdutoBean p1 = new ProdutoBean();
        p1.setValor(200.0);

        ProdutoBean p2 = new ProdutoBean();
        p2.setValor(100.0);

        ProdutoBean p3 = new ProdutoBean();
        p3.setValor(200.0);

        assertTrue(p1.compareTo(p2) > 0, "200 > 100 deve ser positivo");
        assertTrue(p2.compareTo(p1) < 0, "100 < 200 deve ser negativo");
        assertEquals(0, p1.compareTo(p3), "200 == 200 deve ser zero");
    }

    @Test
    void compareTo_trataNulls() {
        ProdutoBean pNull = new ProdutoBean();
        pNull.setValor(null);

        ProdutoBean pZero = new ProdutoBean();
        pZero.setValor(0.0);

        ProdutoBean pNull2 = new ProdutoBean();
        pNull2.setValor(null);

        assertTrue(pNull.compareTo(pZero) < 0, "null < 0.0");
        assertTrue(pZero.compareTo(pNull) > 0, "0.0 > null");
        assertEquals(0, pNull.compareTo(pNull2), "null == null");
    }
}
