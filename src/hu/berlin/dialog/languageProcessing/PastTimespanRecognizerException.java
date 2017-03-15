package hu.berlin.dialog.languageProcessing;

/**
 * Created by Duc on 12.02.17.
 */
public class PastTimespanRecognizerException extends Exception{

    static public class PastTimespanRecognizerMissingTimeValueException extends PastTimespanRecognizerException {

    }

    static public class PastTimespanRecognizerMissingTimeUnitException extends PastTimespanRecognizerException {

    }

    static public class PastTimespanRecognizerMissingTimeUnitAndValueException extends PastTimespanRecognizerException {

    }

    static public class PastTimespanRecognizerNoTimeException extends PastTimespanRecognizerException {

    }


}
