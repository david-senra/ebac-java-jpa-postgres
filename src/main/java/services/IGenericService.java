package services;

import domain.IPersistente;
import exception.DaoException;
import exception.MaisDeUmRegistroException;
import exception.TableException;
import exception.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericService <T extends IPersistente, E extends Serializable> {
    public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DaoException;

    public void excluir(T entity) throws DaoException;

    public T alterar(T entity) throws TipoChaveNaoEncontradaException, DaoException;

    public T consultar(E valor) throws MaisDeUmRegistroException, TableException, DaoException;

    public Collection<T> buscarTodos() throws DaoException;
}
