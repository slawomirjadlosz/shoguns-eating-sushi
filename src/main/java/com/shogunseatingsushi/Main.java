package com.shogunseatingsushi;

import com.shogunseatingsushi.exception.InvalidTableSeatsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    public static void main(String[] args) {

        // Define program assumptions
        final int NO_OF_SHOGUNS = 5;
        final int NO_OF_SEATS = 5;

        Logger logger = LogManager.getLogger(Main.class);

        // Creating "story environment"
        logger.info("-----------------------------------------------------------------------");
        try {
            Table table = new Table(NO_OF_SEATS);
            Chamberlain chamberlain = new Chamberlain(table);
            Itamae itamae = new Itamae(table);
            Thread itamaeThread = new Thread(itamae);

            // Create desired no of shoguns (each shogun is a separate thread)
            for(int i=0; i<NO_OF_SHOGUNS; i++) {
                SushiType favouriteSushiType = SushiType.values()[i];
                Shogun shogun = new Shogun(favouriteSushiType, table, chamberlain, i+1);
                chamberlain.add(shogun);
                int sitting = table.sitAtTable(shogun);
                if(sitting == Table.NOT_ENOUGH_SEATS) {
                    logger.info("TABLE        -       Shogun (id: " + shogun.getId() + ") can't sit at this table because there are not enough places.");
                }
                if(sitting == Table.SAT) {
                    logger.info("TABLE        -       Shogun (id: " + shogun.getId() + ") has just sat at the table.");
                }
                if(sitting == Table.ALREADY_SAT) {
                    logger.info("TABLE        -       Shogun (id: " + shogun.getId() + ") is already sitting at the table.");
                }
                Thread shogunThread = new Thread(shogun);
                shogunThread.start();
            }
            itamaeThread.start();
        } catch (InvalidTableSeatsException e) {
            logger.info(e);
        }
        logger.info("-----------------------------------------------------------------------");
    }
}