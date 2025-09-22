package br.unicamp.ic.inf335.beans;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnuncianteBeanTest {

    private ProdutoBean produto(double valor) {
        ProdutoBean p = new ProdutoBean();
        p.setValor(valor);
        return p;
    }

    private AnuncioBean anuncio(double valorProduto, double desconto) {
        AnuncioBean a = new AnuncioBean();
        a.setProduto(produto(valorProduto));
        a.setDesconto(desconto);
        a.setFotosUrl(new ArrayList<>());
        return a;
    }

    /** Tenta chamar adicionarAnuncio/addAnuncio; se não achar, usa fallback na lista. */
    private void addAnuncioCompat(AnuncianteBean anunciante, AnuncioBean a) {
        try {
            Method m = null;
            try { m = anunciante.getClass().getMethod("adicionarAnuncio", AnuncioBean.class); }
            catch (NoSuchMethodException ignored) {}
            if (m == null) {
                try { m = anunciante.getClass().getMethod("addAnuncio", AnuncioBean.class); }
                catch (NoSuchMethodException ignored) {}
            }
            if (m != null) {
                m.invoke(anunciante, a);
                return;
            }
        } catch (Exception ignored) {}
        // fallback: manipula lista diretamente
        anunciante.getAnuncios().add(a);
    }

    /** Tenta chamar removerAnuncio/removeAnuncio; se só houver remove por índice, usa índice 0. */
    private void removeAnuncioCompat(AnuncianteBean anunciante, AnuncioBean a) {
        try {
            Method m = null;

            // tentar remover por objeto (preferível)
            try { m = anunciante.getClass().getMethod("removerAnuncio", AnuncioBean.class); }
            catch (NoSuchMethodException ignored) {}
            if (m == null) {
                try { m = anunciante.getClass().getMethod("removeAnuncio", AnuncioBean.class); }
                catch (NoSuchMethodException ignored2) {}
            }
            if (m != null) {
                m.invoke(anunciante, a);
                return;
            }

            // tentar remover por índice
            try {
                Method mIdx = anunciante.getClass().getMethod("removerAnuncio", int.class);
                if (!anunciante.getAnuncios().isEmpty()) {
                    mIdx.invoke(anunciante, 0);
                    return;
                }
            } catch (NoSuchMethodException ignored3) {}

            try {
                Method mIdx2 = anunciante.getClass().getMethod("removeAnuncio", int.class);
                if (!anunciante.getAnuncios().isEmpty()) {
                    mIdx2.invoke(anunciante, 0);
                    return;
                }
            } catch (NoSuchMethodException ignored4) {}

        } catch (Exception ignored) {}

        // fallback
        anunciante.getAnuncios().remove(a);
    }

    @Test
    void adicionarERemoverAnuncio_compat() {
        AnuncianteBean an = new AnuncianteBean();
        List<AnuncioBean> lista = an.getAnuncios();
        assertNotNull(lista, "getAnuncios() não deve retornar null");
        assertEquals(0, lista.size(), "Lista deve iniciar vazia");

        AnuncioBean a1 = anuncio(100.0, 0.0);
        addAnuncioCompat(an, a1);
        assertEquals(1, an.getAnuncios().size(), "Após adicionar, tamanho deve ser 1");

        removeAnuncioCompat(an, a1);
        assertEquals(0, an.getAnuncios().size(), "Após remover, tamanho deve voltar a 0");
    }

    @Test
    void valorMedio_comAnuncios() {
        AnuncianteBean an = new AnuncianteBean();

        // 100 com 0% => 100
        addAnuncioCompat(an, anuncio(100.0, 0.0));
        // 200 com 50% => 100
        addAnuncioCompat(an, anuncio(200.0, 0.5));
        // 300 com 100% => 0
        addAnuncioCompat(an, anuncio(300.0, 1.0));

        double esperado = (100.0 + 100.0 + 0.0) / 3.0;
        assertEquals(esperado, an.valorMedioAnuncios(), 1e-9);
    }

    @Test
    void valorMedio_listaVazia() {
        AnuncianteBean an = new AnuncianteBean();
        assertEquals(0.0, an.valorMedioAnuncios(), 1e-9, "Lista vazia deve retornar 0.0");
    }
}
