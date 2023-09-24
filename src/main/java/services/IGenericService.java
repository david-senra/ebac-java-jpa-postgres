package services;

import domain.IPersistente;
import exception.DaoException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericService <T extends IPersistente, E extends Serializable> {
    T cadastrar(T entity) throws DaoException;

    void excluir(T entity) throws DaoException;

    T alterar(T entity) throws DaoException;

    T consultar(E valor) throws DaoException;

    Collection<T> buscarTodos() throws DaoException;
}
