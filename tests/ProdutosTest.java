import dao.ProdutoDao;
import domain.Produto;
import mock.MockProduto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.RollbackException;
import java.math.BigDecimal;
import java.util.List;

public class ProdutosTest {
    private ProdutoDao produtoDao;
    private Produto mockProduto;
    private Produto mockProduto2;
    private Produto mockProdutoIdRepetido;
    private Produto mockProdutoIdDiferenteTudoRepetido;

    @Before
    public void init() throws Exception {
        produtoDao = new ProdutoDao();
        MockProduto mock = new MockProduto();
        mockProduto = mock.getMockProduto();
        mockProduto2 = mock.getMockProdutoNaoCadastrado();
        mockProdutoIdRepetido = mock.getMockProdutoIdRepetido(mockProduto);
        mockProdutoIdDiferenteTudoRepetido = mock.getMockProdutoIdDiferenteTudoRepetido(mockProduto);
        mockProduto.setId(null);
        mockProduto2.setId(null);
        mockProdutoIdRepetido.setId(null);
        mockProdutoIdDiferenteTudoRepetido.setId(null);
    }

    @After
    public void end() throws Exception {
        produtoDao.excluirTodos();
    }

    @Test
    public void cadastrarProdutoNovoExpectSuccess() throws Exception {
        Produto produtoCadastrado1 = produtoDao.cadastrar(mockProduto);
        Assert.assertNotNull(produtoCadastrado1);
        Produto produtoEncontrado = produtoDao.consultar(mockProduto.getId());
        Assert.assertNotNull(produtoEncontrado);
        Assert.assertEquals(produtoEncontrado.getDescricao(), mockProduto.getDescricao());
        Assert.assertEquals(produtoEncontrado.getNome(), mockProduto.getNome());
    }

    @Test(expected = RollbackException.class)
    public void cadastrarProdutoIdJaExistenteExpectError() throws Exception {
        Produto produtoCadastrado1 = produtoDao.cadastrar(mockProduto);
        Assert.assertNotNull(produtoCadastrado1);
        if (mockProduto.getCodigo().equals(mockProdutoIdRepetido.getCodigo())) {
            produtoDao.cadastrar(mockProdutoIdRepetido);
        }
    }

    @Test
    public void cadastrarProdutoIdDiferenteMesmoNomeExpectSuccess() throws Exception {
        Produto produtoCadastrado1 = produtoDao.cadastrar(mockProduto);
        Assert.assertNotNull(produtoCadastrado1);
        Produto produtoCadastrado2 = produtoDao.cadastrar(mockProdutoIdDiferenteTudoRepetido);
        Assert.assertNotNull(produtoCadastrado2);
        Produto clienteEncontrado = produtoDao.consultar(mockProduto.getId());
        Assert.assertNotNull(clienteEncontrado);
        Assert.assertEquals(clienteEncontrado.getDescricao(), mockProduto.getDescricao());
        Assert.assertEquals(clienteEncontrado.getNome(), mockProduto.getNome());
        Produto clienteEncontrado2 = produtoDao.consultar(mockProdutoIdDiferenteTudoRepetido.getId());
        Assert.assertNotNull(clienteEncontrado2);
        Assert.assertEquals(clienteEncontrado2.getDescricao(), mockProdutoIdDiferenteTudoRepetido.getDescricao());
        Assert.assertEquals(clienteEncontrado2.getNome(), mockProdutoIdDiferenteTudoRepetido.getNome());
        produtoDao.excluir(mockProduto);
        List<Produto> listaClientes = (List<Produto>) produtoDao.buscarTodos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(1, listaClientes.size());
        produtoDao.excluir(mockProdutoIdDiferenteTudoRepetido);
        listaClientes = (List<Produto>) produtoDao.buscarTodos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(0, listaClientes.size());
    }

    @Test
    public void buscarTodosTest() throws Exception {
        Produto produtoCadastrado1 = produtoDao.cadastrar(mockProduto);
        Assert.assertNotNull(produtoCadastrado1);
        Produto produtoCadastrado2 = produtoDao.cadastrar(mockProduto2);
        Assert.assertNotNull(produtoCadastrado2);
        List<Produto> listaProdutos = (List<Produto>) produtoDao.buscarTodos();
        Assert.assertNotNull(listaProdutos);
        Assert.assertEquals(2, listaProdutos.size());
        listaProdutos.forEach(produto -> Assert.assertTrue(produto.getNome().equals(mockProduto.getNome())
                || produto.getNome().equals(mockProduto2.getNome())));
    }

    @Test
    public void atualizarTest() throws Exception {
        Produto produtoCadastrado1 = produtoDao.cadastrar(mockProduto);
        Assert.assertNotNull(produtoCadastrado1);

        Produto produtoEncontrado = produtoDao.consultar(mockProduto.getId());
        Assert.assertNotNull(produtoEncontrado);
        Assert.assertEquals(produtoEncontrado.getNome(), mockProduto.getNome());
        Assert.assertEquals(produtoEncontrado.getDescricao(), mockProduto.getDescricao());
        Assert.assertEquals(produtoEncontrado.getPreco().stripTrailingZeros(), mockProduto.getPreco().stripTrailingZeros());

        produtoEncontrado.setNome("Mario Bros");
        produtoEncontrado.setDescricao("Vulgo Luigi");
        produtoEncontrado.setPreco(BigDecimal.valueOf(99.99));
        produtoDao.alterar(produtoEncontrado);

        Produto produtoEncontrado2 = produtoDao.consultar(produtoEncontrado.getId());
        Assert.assertNotNull(produtoEncontrado2);
        Assert.assertEquals(produtoEncontrado.getNome(), produtoEncontrado2.getNome());
        Assert.assertEquals(produtoEncontrado.getDescricao(), produtoEncontrado2.getDescricao());
        Assert.assertEquals(produtoEncontrado.getPreco(), produtoEncontrado2.getPreco());
    }

}
