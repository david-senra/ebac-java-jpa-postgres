package dao;

import domain.Venda;
import exception.DaoException;
import exception.TipoChaveNaoEncontradaException;

public interface IVendaDao {
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DaoException;
    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DaoException;
    public Venda consultarComCollection(Long id);
}
