package hu.berlin.dialog.languageProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;

import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.api.Synset;
import de.tuebingen.uni.sfs.germanet.relatedness.Relatedness;
import hu.berlin.dialog.configuration.WatsonLanguageClassifierConfig2;
import hu.berlin.dialog.languageProcessing.EmploymentClassifier.EmploymentCategory;
import hu.berlin.dialog.languageProcessing.example.GermaNetExample;
import json.JSONException;
import json.JSONObject;

public class IdeaClassifier implements Classifier {

	public enum InnoCategory implements Category {
		INNOVATIVE,
		INNORISKY,
		NOT_INNOVATIVE,
		NOT_RISKY,
		UNSPECIFIED
	}
	
	final private static String IDENTIFIER = "cedf17x168-nlc-2759";
	private NaturalLanguageClassifier classifier;
	
	public IdeaClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword(WatsonLanguageClassifierConfig2.USERNAME, WatsonLanguageClassifierConfig2.PASSWORD);
    }
	
	@Override
	public InnoCategory classify(String input) {
		InnoCategory category;
        Classification result = this.classifier.classify(IdeaClassifier.IDENTIFIER, input).execute();
       
        //if (result.getTopClass().getTopConfidence() < 0.5)  -- sollte keine string zurueckgeben..IdeaClassifier.
        
        switch (result.getTopClass()) {
        case "Innovativ":
            category = InnoCategory.INNOVATIVE;
            break;
        case "Technologieorientiert":
            category = InnoCategory.INNOVATIVE;
            break;
        case "Wissensbasiert":
            category = InnoCategory.INNOVATIVE;
            break;
        case "nicht innovativ":
            category = InnoCategory.NOT_INNOVATIVE;
            break;
        case "nicht riskant":
            category = InnoCategory.NOT_RISKY;
            break;
        case "riskant":
            category = InnoCategory.INNORISKY;
            break;
        default:
            category = InnoCategory.UNSPECIFIED;
    }

    return category;
}

	public static void main(String[] args) {
        System.out.println("Starting testing method for IdeaClassifier");

        IdeaClassifier classifier = new IdeaClassifier();
        InnoCategory category = classifier.classify("Ich finde unsere Idee ziemlich innovativ.");
        System.out.println(category);
    }
	
}
