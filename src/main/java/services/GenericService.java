package services;

import dao.generic.IGenericDao;
import domain.IPersistente;
import exception.DaoException;
import exception.MaisDeUmRegistroException;
import exception.TableException;
import exception.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;

public abstract class GenericService <T extends IPersistente, E extends Serializable> implements IGenericService<T, E> {

    protected IGenericDao<T, E> dao;

	public GenericService(IGenericDao<T, E> dao) {
            this.dao = dao;
    }

    @Override
    public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DaoException {
        return this.dao.cadastrar(entity);
    }

    @Override
    public void excluir(T entity) throws DaoException {
        this.dao.excluir(entity);
    }

    @Override
    public T alterar(T entity) throws TipoChaveNaoEncontradaException, DaoException {
        return this.dao.alterar(entity);
    }

    @Override
    public T consultar(E valor) throws MaisDeUmRegistroException, TableException, DaoException {
        return this.dao.consultar(valor);
    }

    @Override
    public Collection<T> buscarTodos() throws DaoException {
        return this.dao.buscarTodos();
    }
}
