package dao.generic;

import domain.IPersistente;
import exception.DaoException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class GenericDao <T extends IPersistente, E extends Serializable> implements IGenericDao <T,E> {
    protected EntityManagerFactory entityManagerFactory;

    protected EntityManager entityManager;

    protected final Class<T> persistenteClass;

    public GenericDao(Class<T> persistenteClass) {
        this.persistenteClass = persistenteClass;
    }

    @Override
    public T cadastrar(T entity) throws DaoException {
        openConnection();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        closeConnection();
        return entity;
    }

    @Override
    public void excluir(T entity) throws DaoException {
        openConnection();
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
        closeConnection();
    }

    @Override
    public T alterar(T entity) {
        openConnection();
        entity = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        closeConnection();
        return entity;
    }

    @Override
    public T consultar(E valor) {
        openConnection();
        T entity = entityManager.find(this.persistenteClass, valor);
        entityManager.getTransaction().commit();
        closeConnection();
        return entity;
    }

    @Override
    public Collection<T> buscarTodos() {
        openConnection();
        List<T> list =
                entityManager.createQuery(getSelectSql(), this.persistenteClass).getResultList();
        closeConnection();
        return list;
    }

    @Override
    public void excluirTodos() {
        openConnection();
        List<T> list = entityManager.createQuery(getSelectSql(), this.persistenteClass).getResultList();
        if (list.size() > 0) {
            list.forEach(element -> {
                try {
                    excluir(element);
                } catch (DaoException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        closeConnection();
    }

    protected void openConnection() {
        entityManagerFactory =
                Persistence.createEntityManagerFactory("ExemploJPA");
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    protected void closeConnection() {
        entityManager.close();
        entityManagerFactory.close();
    }

    protected String getSelectSql() {
        return "select obj from " +
                this.persistenteClass.getSimpleName() +
                " obj";
    }
}
