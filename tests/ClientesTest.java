import dao.ClienteDao;
import dao.IClienteDao;
import domain.Cliente;
import mock.MockCliente;
import org.junit.*;

import javax.persistence.RollbackException;
import java.util.List;

public class ClientesTest {

    private IClienteDao clienteDao;
    private Cliente mockCliente;
    private Cliente mockCliente2;
    private Cliente mockClienteRepetido2;
    private Cliente mockClienteIdDiferenteTudoRepetido;

    @Before
    public void init() {
        clienteDao = new ClienteDao();
        MockCliente mock = new MockCliente();
        mockCliente = mock.getMockCliente();
        mockCliente2 = mock.getMockClienteNaoCadastrado();
        Cliente mockClienteRepetido = mock.getMockClienteIdRepetido(mockCliente);
        mockClienteRepetido.setId(mockCliente.getId());
        mockClienteRepetido2 = mock.getMockClienteCPFRepetido(mockCliente);
        mockClienteIdDiferenteTudoRepetido = mock.getMockClienteIdDiferenteTudoRepetido(mockCliente);
        mockCliente.setId(null);
        mockCliente2.setId(null);
        mockClienteRepetido.setId(null);
        mockClienteRepetido2.setId(null);
        mockClienteIdDiferenteTudoRepetido.setId(null);
    }

    @After
    public void end() throws Exception {
        clienteDao.excluirTodos();
    }

    @Test
    public void cadastrarClienteNovoExpectSuccess() throws Exception {
        Cliente clienteCadastrado = clienteDao.cadastrar(mockCliente);
        Assert.assertNotNull(clienteCadastrado);
        Cliente clienteEncontrado = clienteDao.consultar(mockCliente.getId());
        Assert.assertNotNull(clienteEncontrado);
        Assert.assertEquals(clienteEncontrado.getCpf(), mockCliente.getCpf());
        Assert.assertEquals(clienteEncontrado.getNome(), mockCliente.getNome());
    }

    @Test(expected = RollbackException.class)
    public void cadastrarClienteIdJaExistenteExpectError() throws Exception {
        Cliente clienteCadastrado = clienteDao.cadastrar(mockCliente);
        Assert.assertNotNull(clienteCadastrado);
        if (mockCliente.getCpf().equals(mockClienteRepetido2.getCpf())) {
            clienteDao.cadastrar(mockClienteRepetido2);
        }
    }

    @Test
    public void cadastrarClienteIdDiferenteMesmoNomeExpectSuccess() throws Exception {
        Cliente clienteCadastrado1 = clienteDao.cadastrar(mockCliente);
        Assert.assertNotNull(clienteCadastrado1);
        Cliente clienteCadastrado2 = clienteDao.cadastrar(mockClienteIdDiferenteTudoRepetido);
        Assert.assertNotNull(clienteCadastrado2);
        Cliente clienteEncontrado = clienteDao.consultar(mockCliente.getId());
        Assert.assertNotNull(clienteEncontrado);
        Assert.assertEquals(clienteEncontrado.getCpf(), mockCliente.getCpf());
        Assert.assertEquals(clienteEncontrado.getNome(), mockCliente.getNome());
        Cliente clienteEncontrado2 = clienteDao.consultar(mockClienteIdDiferenteTudoRepetido.getId());
        Assert.assertNotNull(clienteEncontrado2);
        Assert.assertEquals(clienteEncontrado2.getCpf(), mockClienteIdDiferenteTudoRepetido.getCpf());
        Assert.assertEquals(clienteEncontrado2.getNome(), mockClienteIdDiferenteTudoRepetido.getNome());
        clienteDao.excluir(mockCliente);
        List<Cliente> listaClientes = (List<Cliente>) clienteDao.buscarTodos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(1, listaClientes.size());
        clienteDao.excluir(mockClienteIdDiferenteTudoRepetido);
        listaClientes = (List<Cliente>) clienteDao.buscarTodos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(0, listaClientes.size());
    }

    @Test
    public void buscarTodosTest() throws Exception {
        Cliente clienteCadastrado1 = clienteDao.cadastrar(mockCliente);
        Assert.assertNotNull(clienteCadastrado1);
        Cliente clienteCadastrado2 = clienteDao.cadastrar(mockCliente2);
        Assert.assertNotNull(clienteCadastrado2);
        List<Cliente> listaClientes = (List<Cliente>) clienteDao.buscarTodos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(2, listaClientes.size());
        listaClientes.forEach(cliente -> Assert.assertTrue(cliente.getNome().equals(mockCliente.getNome())
                || cliente.getNome().equals(mockCliente2.getNome())));
    }

    @Test
    public void atualizarTest() throws Exception {
        Cliente clienteCadastrado1 = clienteDao.cadastrar(mockCliente);
        Assert.assertNotNull(clienteCadastrado1);

        Cliente clienteEncontrado = clienteDao.consultar(mockCliente.getId());
        Assert.assertNotNull(clienteEncontrado);
        Assert.assertEquals(clienteEncontrado.getNome(), mockCliente.getNome());
        Assert.assertEquals(clienteEncontrado.getCpf(), mockCliente.getCpf());

        clienteEncontrado.setNome("Mario Bros");
        clienteEncontrado.setSobrenome("Vulgo Luigi");
        clienteEncontrado.setCpf("000-000-000.00");
        clienteDao.alterar(clienteEncontrado);

        Cliente clienteEncontrado2 = clienteDao.consultar(clienteEncontrado.getId());
        Assert.assertNotNull(clienteEncontrado2);
        Assert.assertEquals(clienteEncontrado.getNome(), clienteEncontrado2.getNome());
        Assert.assertEquals(clienteEncontrado.getTelefone(), clienteEncontrado2.getTelefone());
        Assert.assertEquals(clienteEncontrado.getCpf(), clienteEncontrado2.getCpf());
    }
}
