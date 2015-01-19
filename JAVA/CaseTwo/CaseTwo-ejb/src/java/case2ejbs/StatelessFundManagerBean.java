package case2ejbs;

import java.math.BigDecimal;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/*
Purpoase: Example EJB to test out concept
Author           Date              Description
**********************************************
Nick             Nov. 7 2014      Initial Implementation 
*/

@Stateless
@LocalBean
public class StatelessFundManagerBean {
    
    public BigDecimal addFunds(BigDecimal balance, BigDecimal amount){
        balance = balance.add(amount);
        return balance;
    }
    
    public BigDecimal withdrawFunds(BigDecimal balance, BigDecimal amount){
        if (balance.doubleValue() < 0){
            return new BigDecimal("0.00");
        } else {
            balance = balance.subtract(amount);
            return balance;
        }
    }
}
