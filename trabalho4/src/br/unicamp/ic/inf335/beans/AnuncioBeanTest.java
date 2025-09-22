package br.unicamp.ic.inf335.beans;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.net.URL;

public class AnuncioBeanTest {

    private ProdutoBean produto(double valor) {
        ProdutoBean p = new ProdutoBean();
        p.setValor(valor);
        return p;
    }

    private AnuncioBean anuncio(double valorProduto, Double desconto) {
        AnuncioBean a = new AnuncioBean();
        a.setProduto(produto(valorProduto));
        a.setFotosUrl(new ArrayList<URL>());
        a.setDesconto(desconto);
        return a;
    }

    @Test
    void getValor_semDesconto() {
        AnuncioBean a = anuncio(100.0, 0.0);
        assertEquals(100.0, a.getValor(), 1e-9);
    }

    @Test
    void getValor_comDesconto20() {
        AnuncioBean a = anuncio(100.0, 0.2);
        assertEquals(80.0, a.getValor(), 1e-9);
    }

    @Test
    void getValor_desconto100() {
        AnuncioBean a = anuncio(100.0, 1.0);
        assertEquals(0.0, a.getValor(), 1e-9);
    }

    @Test
    void getValor_trataNulls_e_clamp() {
        // produto null => 0.0
        AnuncioBean a1 = new AnuncioBean();
        a1.setProduto(null);
        a1.setDesconto(0.3);
        assertEquals(0.0, a1.getValor(), 1e-9);

        // desconto null => 0.0
        AnuncioBean a2 = anuncio(50.0, null);
        assertEquals(50.0, a2.getValor(), 1e-9);

        // desconto negativo -> clamp 0.0
        AnuncioBean a3 = anuncio(50.0, -0.5);
        assertEquals(50.0, a3.getValor(), 1e-9);

        // desconto > 1.0 -> clamp 1.0
        AnuncioBean a4 = anuncio(50.0, 2.0);
        assertEquals(0.0, a4.getValor(), 1e-9);
    }
}
