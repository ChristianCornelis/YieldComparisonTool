package project.helpers;

import project.Exceptions;
import project.database.DatabaseClient;

/**
 * Helper class to handle connecting user input to deletion of producer records.
 * Temporary - web app will replace this class.
 */
public class DeletionHandler {
    public static final int DELETE_ALL_RECORDS = 0;
    public static final int DELETE_RECORDS_BY_YEAR = 1;
    public static final int DELETE_RECORDS_BY_RANGE = 2;
    private String producer;
    private InputHandler inputHandler;
    private DatabaseClient dbClient;

    /**
     * Constructor.
     * @param producer the producer.
     * @param ih inputhandler instance
     * @param dbc databaseclient instance
     */
    public DeletionHandler(String producer, InputHandler ih, DatabaseClient dbc) {
        this.producer = producer;
        this.inputHandler = ih;
        this.dbClient = dbc;
    }

    /**
     * Method to delete a database producer record. Wraps the DBClient method removeYieldByYearAndProducer.
     */
    public void deleteProducerRecord() {
        int[] validActions = {DELETE_RECORDS_BY_YEAR, PromptHelper.CANCEL_TASK};
        int action = inputHandler.chooseAction(inputHandler.getProducerDeletionPrompt(), validActions);
        switch (action) {
            case DELETE_RECORDS_BY_YEAR:
                System.out.println(inputHandler.getProducerDeletionByYearPrompt());
                int year = Integer.parseInt(inputHandler.getBasicInput());
                try {
                    dbClient.removeYieldByYearAndProducer(year, producer);
                    System.out.println(inputHandler.getProducerDeletionSuccessfulPrompt(year, producer));
                } catch (Exceptions.DatabaseDeletionException e) {
                    System.out.println(e.getMessage());
                } catch (Exceptions.NoDatabaseRecordsRemovedException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case PromptHelper.CANCEL_TASK:
                return;

        }
    }
}
