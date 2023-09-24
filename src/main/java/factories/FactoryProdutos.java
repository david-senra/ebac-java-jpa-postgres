package factories;

import domain.Produto;
import domain.ProdutoQuantidade;
import domain.Venda;

public class FactoryProdutos {

    public static void criarProdutoQuantidade(Produto produto, Venda venda, Integer quantidade) {
        ProdutoQuantidade prod = new ProdutoQuantidade();
        prod.setVenda(venda);
        prod.setProduto(produto);
        prod.adicionar(quantidade);
        venda.addProdutoQuantidade(prod);
    }
}
