package hu.berlin.dialog.evaluation;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.user.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duc on 09.02.17.
 */
public class AssistanceProgramsEvaluator {

    public interface AssistanceProgramInfo {
        String toString();
        String description();
        String url();
    }

    public enum AssistancePrograms implements AssistanceProgramInfo{
        ExistGruenderstipendium {
          public String toString() {
              return "EXIST Gründerstipendium";
          }

          public String description() {
              return "Das EXIST Gründerstipendium ist eines der größten und bekanntesten Förderprogramme in Deutschland. " +
                      "Es unterstützt Studierende, Absolventinnen und Absolventen sowie Wissenschaftlerinnen und Wissenschaftler " +
                      "aus Hochschulen und außeruniversitären Forschungseinrichtungen, die ihre Gründungsidee realisieren und " +
                      "in einen Businessplan umsetzen möchten. Bei den Gründungsvorhaben sollte es sich um innovative " +
                      "technologieorientierte oder wissensbasierte Projekte mit signifikanten Alleinstellungsmerkmalen " +
                      "und guten wirtschaftlichen Erfolgsaussichten handeln.";
          }

          public String url() {
              return "http://www.exist.de/DE/Programm/Exist-Gruenderstipendium/inhalt.html";
          }
        },
        ExistForschungstransfer {
            public String toString() {
                return "EXIST Forschungstransfer";
            }

            public String description() {
                return "Das EXIST Forschungstransfer eignet sich besonders innovative Ideen, die mit einem sehr hohen Risikofaktor" +
                        "verbunden sind. Es besteht aus zwei Phasen. In der ersten Phase geht es um Entwicklungsarbeiten, die die Realisierbarkeit" +
                        "des Projekts untersuchen. Anschließend geht es um die Entwicklung eines Prototypen und am Ende um die eigentliche Gründung" +
                        "des Startups. Ihr könnt mit bis zu 250000€ gefördert werden!\n" +
                        "Gegenstand der Förderung sind weitere Entwicklungsarbeiten, Maßnahmen zur Aufnahme der Geschäftstätigkeit im neu gegründeten " +
                        "Technologieunternehmen sowie die Schaffung der Voraussetzungen für eine externe Unternehmensfinanzierung.";
            }

            public String url() {
                return "http://www.exist.de/DE/Programm/Exist-Forschungstransfer/inhalt.html";
            }
        },
        Gruendungszuschussformel {
            public String toString() {
                return "Gründungszuschuss";
            }

            public String description() {
                return "Arbeitslose, die eine selbstständige Tätigkeit aufnehmen wollen, können einen sogenannten Gründungszuschuss erhalten," +
                        "um ihr Unternehmen erfolgreich gründen zu können.";
            }

            public String url() {
                return "https://www3.arbeitsagentur.de/web/content/DE/BuergerinnenUndBuerger/FinanzielleHilfen/Existenzgruendung/index.htm";
            }
        },
        IBBBerlinInnovation {
            public String toString() {
                return "EXIST Gründerstipendium";
            }

            public String description() {
                return "beshcreibung";
            }

            public String url() {
                return "";
            }
        },
        IBBBerlinInnovationsAssistent {
            public String toString() {
                return "EXIST Gründerstipendium";
            }

            public String description() {
                return "beshcreibung";
            }

            public String url() {
                return "";
            }
        }
    }

    public static List<AssistancePrograms> findSuitableAssistancePrograms(UserProfile profile) {
        boolean hasPHD = profile.getValueForPredicate(PredicateConstants.hasPHD);
        boolean isWimi = profile.getValueForPredicate(PredicateConstants.isWiMi);
        boolean isProf = profile.getValueForPredicate(PredicateConstants.isProfessor);
        boolean isInno = profile.getValueForPredicate(PredicateConstants.isInnovative);
        boolean isKnowledgeBased = profile.getValueForPredicate(PredicateConstants.isKnowledgeBased);
        boolean isTechno = profile.getValueForPredicate(PredicateConstants.isTechnologyOriented);
        boolean isRecentGraduate = profile.getValueForPredicate(PredicateConstants.isRecentGraduate);
        boolean isStudent = profile.getValueForPredicate(PredicateConstants.isStudent);
        boolean isUnemployed = profile.getValueForPredicate(PredicateConstants.isUnemployed);
        boolean failureIsPossible = profile.getValueForPredicate(PredicateConstants.failureIsPossible);

        List <AssistancePrograms> suitablePrograms = new ArrayList<>();

        if ((isRecentGraduate) &&
                (isStudent) &&
                (isInno && (isKnowledgeBased || isTechno)) ) {
            suitablePrograms.add(AssistancePrograms.ExistGruenderstipendium);
        }

        if (hasPHD &&
                (isWimi || isProf) &&
                (isInno && (isKnowledgeBased || isTechno))) {
            suitablePrograms.add(AssistancePrograms.ExistForschungstransfer);
        }

        if ((isUnemployed)) {
            suitablePrograms.add(AssistancePrograms.Gruendungszuschussformel);
        }

        if ((failureIsPossible && isInno)) {
            suitablePrograms.add(AssistancePrograms.IBBBerlinInnovation);
        }

        if ((isInno)) {
            suitablePrograms.add(AssistancePrograms.IBBBerlinInnovationsAssistent);
        }

        return suitablePrograms;
    }

}
