package dao.generic;

import domain.IPersistente;
import exception.DaoException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericDao<T extends IPersistente, E extends Serializable> {
    T cadastrar(T entity) throws DaoException;
    void excluir(T entity) throws DaoException;
    void excluirTodos() throws DaoException;
    T alterar(T entity) throws DaoException;
    T consultar(E id) throws DaoException;
    Collection<T> buscarTodos() throws DaoException;
}
