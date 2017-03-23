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
              return "Das EXIST Gründerstipendium ist eines der größten und bekanntesten Förderprogramme in Deutschland. \n" +
                      "Es unterstützt Studierende, Absolventinnen und Absolventen sowie Wissenschaftlerinnen und Wissenschaftler \n" +
                      "aus Hochschulen und außeruniversitären Forschungseinrichtungen, die ihre Gründungsidee realisieren und \n" +
                      "in einen Businessplan umsetzen möchten. Bei den Gründungsvorhaben sollte es sich um innovative \n" +
                      "technologieorientierte oder wissensbasierte Projekte mit signifikanten Alleinstellungsmerkmalen \n" +
                      "und guten wirtschaftlichen Erfolgsaussichten handeln.\n";
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
                return "Das EXIST Forschungstransfer eignet sich besonders innovative Ideen, die mit einem sehr hohen Risikofaktor \n" +
                        "verbunden sind. Es besteht aus zwei Phasen. In der ersten Phase geht es um Entwicklungsarbeiten, die die Realisierbarkeit \n" +
                        "des Projekts untersuchen. Anschließend geht es um die Entwicklung eines Prototypen und am Ende um die eigentliche Gründung \n" +
                        "des Startups. Ihr könnt mit bis zu 250000€ gefördert werden! \n" +
                        "Gegenstand der Förderung sind weitere Entwicklungsarbeiten, Maßnahmen zur Aufnahme der Geschäftstätigkeit im neu gegründeten \n" +
                        "Technologieunternehmen sowie die Schaffung der Voraussetzungen für eine externe Unternehmensfinanzierung.\n";
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
                return "Arbeitslose, die eine selbstständige Tätigkeit aufnehmen wollen, können einen sogenannten Gründungszuschuss erhalten,\n" +
                        "um ihr Unternehmen erfolgreich gründen zu können. Ziel der Förderung ist es, Menschen aus der Arbeitslosigkeit zu führen.\n";
            }

            public String url() {
                return "https://www3.arbeitsagentur.de/web/content/DE/BuergerinnenUndBuerger/FinanzielleHilfen/Existenzgruendung/index.htm";
            }
        },
        IBBBerlinInnovation { 
            public String toString() {
                return "IBB Berlin Innovativ";
            }

            public String description() {
                return "Sie benötigen ein Finanzierungsangebot für Ihr Vorhaben und erfüllen eines unserer Innovationskriterien?\n" +
                        " Mit Berlin Innovativ können Darlehen für Investitionen und Betriebsmittel bis 2 Mio. EUR im Hausbankenverfahren \n" +
                        "vergeben werden. Ihr Vorteil: Eine Haftungsfreistellung der Hausbank in Höhe von 70 Prozent durch die IBB. \n" +
                        "Mit diesem Förderprogramm sollen besonders innovative Berliner Unternehmen und Startups angesprochen und bei\n" +
                        " der Stärkung Ihrer Wettbewerbsfähigkeit sowie Erschließung neuer Märkte unterstützt werden.\n";
            }

            public String url() {
                return "https://www.ibb.de/de/foerderprogramme/berlin-innovativ.html";
            }
        },
        IBBBerlinInnovationsAssistent {
            public String toString() {
                return "IBB Berlin Innovationsassistent";
            }

            public String description() {
                return "Sie sind in der Gründungsphase und wollen sich mit Ihrem Unternehmen erfolgreich \n"
                		+ "am Markt positionieren? Oder sind Sie ein etabliertes Unternehmen und wollen \n"
                		+ "expandieren? Ein Weg, um geeignete Grundlagen dafür zu schaffen, ist die \n"
                		+ "Einstellung von qualifizierten Universitäts- oder \n"
                		+ "(Fach)Hochschulabsolventen/-innen. Mit Personalkostenzuschüssen unterstützt \n"
                		+ "die IBB im Auftrag der Senatsverwaltung für Wirtschaft, Technologie und \n"
                		+ "Forschung die Einstellung von Innovationsassistenten/-innen.";
            }

            public String url() {
                return "https://www.ibb.de/de/foerderprogramme/innovationsassistent-in.html";
            }
        }
    }

    public static List<AssistancePrograms> findSuitableAssistancePrograms(UserProfile profile) {
        boolean hasPHD = profile.getValueForPredicate(PredicateConstants.hasPHD);
        boolean isScientist = profile.getValueForPredicate(PredicateConstants.isScientist);
        boolean isInno = profile.getValueForPredicate(PredicateConstants.isInnovative);
        //boolean isKnowledgeBased = profile.getValueForPredicate(PredicateConstants.isKnowledgeBased);
        //boolean isTechno = profile.getValueForPredicate(PredicateConstants.isTechnologyOriented);
        boolean isRecentGraduate = profile.getValueForPredicate(PredicateConstants.isRecentGraduate);
        boolean isStudent = profile.getValueForPredicate(PredicateConstants.isStudent);
        boolean isUnemployed = profile.getValueForPredicate(PredicateConstants.isUnemployed);
        boolean failureIsPossible = profile.getValueForPredicate(PredicateConstants.failureIsPossible);
        boolean wasFounded = profile.getValueForPredicate(PredicateConstants.companyHasBeenFounded);
        //boolean threeMembersOrFewer = profile.getValueForPredicate(PredicateConstants.threeMembersOrFewer);
        //boolean four = profile.getValueForPredicate(PredicateConstants.fourMembers);
        //boolean moreThanFourMembers = profile.getValueForPredicate(PredicateConstants.moreThanFourMembers);
        boolean lookingForMoney = profile.getValueForPredicate(PredicateConstants.lookingForMoney);
        boolean lookingForEmployees = profile.getValueForPredicate(PredicateConstants.lookingForEmployees);
        boolean fastGrowth = profile.getValueForPredicate(PredicateConstants.fastGrowth);
        boolean companyIsYoungerThan12Years = profile.getValueForPredicate(PredicateConstants.companyIsYoungerOrEqualThan12Years);
        boolean costs5pc = profile.getValueForPredicate(PredicateConstants.costsAtLeast5PercentOnceInLast3Years);
        boolean wonInnoPrize = profile.getValueForPredicate(PredicateConstants.wonInnovationPrize);
        boolean claimsPatent = profile.getValueForPredicate(PredicateConstants.claimsPatent);

        int teamsize = 0;
        boolean teamSizeExists = profile.doesValueAtPredicateExist(PredicateConstants.teamSize);
        if (teamSizeExists) teamsize = profile.getIntForPredicate(PredicateConstants.teamSize);

        List <AssistancePrograms> suitablePrograms = new ArrayList<>();

        if ((isRecentGraduate) ||
                (isStudent) &&
                (isInno /* && (isKnowledgeBased || isTechno)*/ ) &&
                (teamSizeExists && teamsize <= 3) &&
                (!wasFounded)) {
            suitablePrograms.add(AssistancePrograms.ExistGruenderstipendium);
        }

        if (hasPHD &&
                (isScientist) &&
                (isInno /*&& (isKnowledgeBased || isTechno)*/) &&
                (teamSizeExists && teamsize < 5) &&
                (!wasFounded)) {
            suitablePrograms.add(AssistancePrograms.ExistForschungstransfer);
        }

        if ((isUnemployed) &&
                (lookingForMoney)) {
            suitablePrograms.add(AssistancePrograms.Gruendungszuschussformel);
        }

        if ((failureIsPossible && isInno) &&
                (fastGrowth || companyIsYoungerThan12Years || costs5pc || claimsPatent || wonInnoPrize)) {
            suitablePrograms.add(AssistancePrograms.IBBBerlinInnovation);
        }

        if ((isInno && lookingForEmployees)) {
            suitablePrograms.add(AssistancePrograms.IBBBerlinInnovationsAssistent);
        }

        return suitablePrograms;
    }

}
