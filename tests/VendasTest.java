import dao.ClienteDao;
import dao.ProdutoDao;
import dao.VendaDao;
import domain.Cliente;
import domain.Produto;
import domain.Venda;
import exception.DaoException;
import exception.TipoChaveNaoEncontradaException;
import mock.MockCliente;
import mock.MockProduto;
import mock.MockVenda;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendasTest {
    private ClienteDao clienteDao;
    private Cliente mockCliente;


    private ProdutoDao produtoDao;
    private Produto mockProduto;
    private Produto mockProduto2;

    private VendaDao vendaDao;

    private final MockVenda mockVe = new MockVenda();
    private Venda mockVenda;

    @Before
    public void init() throws Exception {
        produtoDao = new ProdutoDao();
        clienteDao = new ClienteDao();
        vendaDao = new VendaDao();
        MockProduto mockPr = new MockProduto();
        MockCliente mockCl = new MockCliente();

        mockCliente = mockCl.getMockCliente();
        mockCliente.setId(null);
        clienteDao.cadastrar(mockCliente);

        mockProduto = mockPr.getMockProduto();
        mockProduto2 = mockPr.getMockProdutoNaoCadastrado();
        mockProduto.setId(null);
        mockProduto2.setId(null);
        produtoDao.cadastrar(mockProduto);
        produtoDao.cadastrar(mockProduto2);
    }

    @After
    public void end() {
        vendaDao.limparProdutosQuantidade();
        vendaDao.excluirTodasVendas();
        clienteDao.excluirTodos();
        produtoDao.excluirTodos();
    }

    @Test
    public void CadastrarAlterarNovaVenda() throws TipoChaveNaoEncontradaException, DaoException {
        mockVenda = mockVe.getVenda01();
        mockVenda.setId(null);
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());
        mockVenda.adicionarProduto(mockProduto, 2);
        mockVenda.adicionarProduto(mockProduto2, 1);

        List<String> listaProdutos = new ArrayList<>();
        mockVenda.getProdutos().forEach(produto -> listaProdutos.add(produto.getProduto().getNome()));
        Assert.assertTrue(listaProdutos.contains(mockProduto.getNome()));
        Assert.assertTrue(listaProdutos.contains(mockProduto2.getNome()));
        vendaDao.cadastrar(mockVenda);

        Venda vendaConsultada = vendaDao.consultarComCollection(mockVenda.getId());
        Assert.assertNotNull(vendaConsultada);

        Assert.assertEquals(mockVenda.getCodigo(), vendaConsultada.getCodigo());
        Assert.assertEquals(mockVenda.getValorTotal(),mockProduto.getPreco().multiply(BigDecimal.valueOf(2)).add(mockProduto2.getPreco()));
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.INICIADA);

        mockVenda.adicionarProduto(mockProduto2, 1);
        Assert.assertEquals(4, (int) mockVenda.getQuantidadeTotalProdutos());
        BigDecimal valorTotalTemporario = mockProduto.getPreco().multiply(BigDecimal.valueOf(2)).add(mockProduto2.getPreco().multiply(BigDecimal.valueOf(2)));
        Assert.assertEquals(mockVenda.getValorTotal(),valorTotalTemporario);

        mockVenda.removerProduto(mockProduto, 1);
        Assert.assertEquals(3, (int) mockVenda.getQuantidadeTotalProdutos());
        BigDecimal valorTotalTemporario2 = mockProduto.getPreco().add(mockProduto2.getPreco().multiply(BigDecimal.valueOf(2)));
        Assert.assertEquals(mockVenda.getValorTotal(),valorTotalTemporario2);

        mockVenda.removerTodosProdutos();
        Assert.assertEquals(0, (int) mockVenda.getQuantidadeTotalProdutos());
        BigDecimal valorZerado = BigDecimal.ZERO;
        Assert.assertEquals(mockVenda.getValorTotal(),valorZerado);
    }

    @Test
    public void CancelarVenda() throws DaoException, TipoChaveNaoEncontradaException {
        mockVenda = mockVe.getVenda01();
        mockVenda.setId(null);
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());

        mockVenda.adicionarProduto(mockProduto, 2);
        mockVenda.adicionarProduto(mockProduto2, 1);

        vendaDao.cadastrar(mockVenda);
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.INICIADA);
        Venda vendaConsultada = vendaDao.consultarComCollection(mockVenda.getId());
        Assert.assertEquals(vendaConsultada.getStatus(), Venda.Status.INICIADA);
        mockVenda.setStatus(Venda.Status.CANCELADA);
        vendaDao.cancelarVenda(mockVenda);
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.CANCELADA);
        Venda vendaConsultada2 = vendaDao.consultarComCollection(mockVenda.getId());
        Assert.assertEquals(vendaConsultada2.getStatus(), Venda.Status.CANCELADA);
    }

    @Test
    public void FinalizarVenda() throws DaoException, TipoChaveNaoEncontradaException {
        mockVenda = mockVe.getVenda01();
        mockVenda.setId(null);
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());

        mockVenda.adicionarProduto(mockProduto, 2);
        mockVenda.adicionarProduto(mockProduto2, 1);

        vendaDao.cadastrar(mockVenda);
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.INICIADA);
        Venda vendaConsultada = vendaDao.consultarComCollection(mockVenda.getId());
        Assert.assertEquals(vendaConsultada.getStatus(), Venda.Status.INICIADA);
        mockVenda.setStatus(Venda.Status.CONCLUIDA);
        vendaDao.finalizarVenda(mockVenda);
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.CONCLUIDA);
        Venda vendaConsultada2 = vendaDao.consultarComCollection(mockVenda.getId());
        Assert.assertEquals(vendaConsultada2.getStatus(), Venda.Status.CONCLUIDA);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void AdicionarProdutoSemEstoqueExpectError() {
        mockVenda = mockVe.getVenda01();
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());

        mockVenda.adicionarProduto(mockProduto, 2);
        Integer numeroEmEstoque = mockProduto.getEstoque();
        Integer acimaDoEstoque = numeroEmEstoque + 2;
        mockVenda.adicionarProduto(mockProduto, acimaDoEstoque); // Adicionando qtd acima do estoque...
    }

    @Test
    public void AdicionarProdutoComEstoque() {
        mockVenda = mockVe.getVenda01();
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());

        mockVenda.adicionarProduto(mockProduto, 2);
        Integer numeroEmEstoque = mockProduto.getEstoque();
        Integer abaixoDoEstoque = numeroEmEstoque - 1;
        mockVenda.adicionarProduto(mockProduto, abaixoDoEstoque); // Adicionando qtd abaixo do estoque...
    }
}
