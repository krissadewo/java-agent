package or.id.greenlabs.instrument.app;

import java.util.logging.Logger;

/**
 * @author krissadewo
 * @date 1/15/21 3:31 PM
 */
public class TaxService {

    private static Logger LOGGER = Logger.getLogger(TaxService.class.getName());

    private static final int account = 10;

    public static void countTax(int amount) throws InterruptedException {
        Thread.sleep(2000L); //processing going on here

        LOGGER.info("[Application] Successful calculate tax of [{" + amount + "}] units!");
    }
}
