package domain.pedidos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carrinho {

    private final Map<Long, ProdutoCarrinho> listaProdutos = new HashMap<>();
    public List<ProdutoCarrinho> getListaProdutos() {
        return listaProdutos.values().stream().toList();
    }
}
