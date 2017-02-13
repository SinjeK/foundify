package hu.berlin.dialog.clause;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.languageProcessing.PastTimespanRecognizer;
import hu.berlin.dialog.languageProcessing.PastTimespanRecognizerException;
import hu.berlin.dialog.languageProcessing.PastTimespanRecognizerException.*;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.user.UserProfile;

/**
 * Created by Duc on 12.02.17.
 */
public class RecentAcademicQualificationClause extends Clause {

    private StanfordCoreNLP coreNLP;
    private String academicTitle;

    private boolean missingTimeUnitError = false;
    private int sec;

    public RecentAcademicQualificationClause(DialogStateController controller, String identifier,
                                             UserProfile profile, StanfordCoreNLP coreNLP, String academicTitle) {
        super(controller, identifier, profile);
        this.coreNLP = coreNLP;
        this.academicTitle = academicTitle;
    }

    @Override
    public void enter() {
        super.enter();

        this.sec = 0;
        this.missingTimeUnitError = false;

        String in = "Wichtig für mich wäre noch zu wissen, wie lange schon du den #ACADEMICTITLE# besitzt?";
        this.put(in.replace("#ACADEMICTITLE#", this.academicTitle));
    }

    @Override
    public void evaluate(String input) {
        super.evaluate(input);

        if (this.missingTimeUnitError) {
            if (PastTimespanRecognizer.timeunits.containsKey(input.toLowerCase())) {
                this.sec = this.sec * PastTimespanRecognizer.timeunits.get(input.toLowerCase());
                int year = sec / (3600 * 24 * 365);
                this.getProfile().setValueForPredicate(year <= 3, PredicateConstants.isRecentGraduate);
                this.put("also seit " + year + " Jahren hast du schon deinen " + this.academicTitle);
                this.leave();
            } else {
                this.put("nochmal die nachfrage: wie lange hast du schon den " + this.academicTitle + "?");
                this.missingTimeUnitError = false;
            }
        } else {
            try {
                this.sec = PastTimespanRecognizer.getNormalizedTimespan(input, this.coreNLP);
                int year = sec / (3600 * 24 * 365);
                this.getProfile().setValueForPredicate(year <= 3, PredicateConstants.isRecentGraduate);
                if (year <= 3) {
                    this.put("das passt doch wunderbar!\nfür manche förderprogramme muss man nämlich einen " +
                            "abschluss in den letzten drei jahren gemacht haben");
                }
                this.leave();
            } catch (PastTimespanRecognizerMissingTimeUnitException e) {
                this.put("Minuten, Stunden, Monate oder Jahre? was meinst du?");
                this.missingTimeUnitError = true;
            } catch (PastTimespanRecognizerException ex)  {
                this.put("entschuldigung ich konnte dich leider nicht verstehen");
                this.missingTimeUnitError = false;
            }
        }
    }

}
