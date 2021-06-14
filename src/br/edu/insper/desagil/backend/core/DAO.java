//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core;

import java.util.Date;
import java.util.List;

import br.edu.insper.desagil.backend.core.exception.APIException;
import br.edu.insper.desagil.backend.core.exception.DBException;

public interface DAO<K, V> {
	boolean exists(K key) throws DBException, APIException;
	boolean exists(List<K> keys) throws DBException, APIException;
	Date create(V value) throws DBException, APIException;
	V retrieve(K key) throws DBException, APIException;
	List<V> retrieve(List<K> keys) throws DBException, APIException;
	List<V> retrieveLt(String key, Object value) throws DBException, APIException;
	List<V> retrieveLeq(String key, Object value) throws DBException, APIException;
	List<V> retrieveEq(String key, Object value) throws DBException, APIException;
	List<V> retrieveGt(String key, Object value) throws DBException, APIException;
	List<V> retrieveGeq(String key, Object value) throws DBException, APIException;
	List<V> retrieveIn(String key, List<Object> values) throws DBException, APIException;
	List<V> retrieveAll() throws DBException, APIException;
	Date update(V value) throws DBException, APIException;
	Date delete(K key) throws DBException, APIException;
	List<Date> delete(List<K> keys) throws DBException, APIException;
	List<Date> deleteAll() throws DBException, APIException;
}
