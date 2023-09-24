package dao;

import dao.generic.GenericDao;
import domain.Cliente;
import domain.Produto;
import domain.ProdutoQuantidade;
import domain.Venda;
import exception.DaoException;
import exception.TipoChaveNaoEncontradaException;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class VendaDao extends GenericDao<Venda, String> implements IVendaDao {

    public VendaDao() {
        super(Venda.class);
    }

    @Override
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DaoException {
        super.alterar(venda);
    }

    @Override
    public void cancelarVenda(Venda venda) {
        super.alterar(venda);
    }

    @Override
    public void excluir(Venda entity) throws DaoException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public Venda cadastrar(Venda entity) throws TipoChaveNaoEncontradaException, DaoException {
        try {
            openConnection();
            entity.getProdutos().forEach(prod -> {
                Produto produto = entityManager.merge(prod.getProduto());
                prod.setProduto(produto);
            });
            Cliente cliente = entityManager.merge(entity.getCliente());
            entity.setCliente(cliente);
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            closeConnection();
            return entity;
        } catch (Exception e) {
            throw new DaoException("ERRO SALVANDO VENDA ", e);
        }
    }

    private void excluirVenda (Venda entity) {
        openConnection();
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
        closeConnection();
    }

    private void excluirProdutoQuantidade (ProdutoQuantidade entity) {
        openConnection();
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
        closeConnection();
    }

    public void excluirTodasVendas () {
        openConnection();
        List<Venda> list =
                entityManager.createQuery(getSelectSql(), Venda.class).getResultList();
        if (list.size() > 0) {
            list.forEach(this::excluirVenda);
        }
        closeConnection();
    }

    public void limparProdutosQuantidade () {
        openConnection();
        List<ProdutoQuantidade> list =
                entityManager.createQuery(getSelectVendaSql(), ProdutoQuantidade.class).getResultList();
        if (list.size() > 0) {
            list.forEach(this::excluirProdutoQuantidade);
        }
        closeConnection();
    }

    @Override
    public Venda consultarComCollection(Long id) {
        openConnection();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Venda> query = builder.createQuery(Venda.class);
        Root<Venda> root = query.from(Venda.class);
        root.fetch("cliente");
        root.fetch("produtos");
        query.select(root).where(builder.equal(root.get("id"), id));
        TypedQuery<Venda> tpQuery =
                entityManager.createQuery(query);
        Venda venda = tpQuery.getSingleResult();
        closeConnection();
        return venda;
    }

    protected String getSelectVendaSql() {
        return "select obj from " +
                ProdutoQuantidade.class.getSimpleName() +
                " obj";
    }
}
