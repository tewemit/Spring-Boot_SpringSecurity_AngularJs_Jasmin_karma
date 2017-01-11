package demo.models;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * A DAO for the entity FAQ is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 *
 * @author Tewelle
 */
@Transactional
public interface FaqDao extends CrudRepository<Faq, Long> {

    /**
     * Returns the question details having the passed question or null if none is found.
     *
     * @param  question the question.
     */
    public User findByQuestion(String question);
    public List<User> findByCategory(String category);
    <S extends Faq> S saveAndFlush(S var1);

}
