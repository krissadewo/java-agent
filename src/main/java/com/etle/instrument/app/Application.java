package com.etle.instrument.app;

import java.util.logging.Logger;

/**
 * @author krissadewo
 * @date 1/15/21 3:32 PM
 */
public class Application {

    private static Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws Exception {
        run(args);
    }

    public static void run(String[] args) throws Exception {
        LOGGER.info("[Application] Starting application");
        TaxService.countTax(2500);

        Thread.sleep(10000);

        TaxService.countTax(3000);
    }
}
