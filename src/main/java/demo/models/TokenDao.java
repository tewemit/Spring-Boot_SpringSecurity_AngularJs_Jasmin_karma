package demo.models;

import demo.persistent.Token;
import org.springframework.data.repository.CrudRepository;


/**
 * Created by tewe on 12/5/2016.
 */
public interface TokenDao extends CrudRepository<Token, String> {
}
