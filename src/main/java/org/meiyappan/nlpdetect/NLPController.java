package org.meiyappan.nlpdetect;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NLPController {

    @Autowired
    StanfordCoreNLP pipeline;

    @PostMapping(path = "detectemotion",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONArray> create(@RequestBody JSONObject data) {
        JSONArray array = new JSONArray();
        try {
            String text = data.get("text").toString();
            int sentimentInt;
            String sentimentName;

            Annotation annotation = pipeline.process(text);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                sentimentInt = RNNCoreAnnotations.getPredictedClass(tree);
                sentimentName = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                System.out.println(sentimentName + "\t" + sentimentInt + "\t" + sentence);
                JSONObject object = new JSONObject();
                object.put("sentence",sentence.toString());
                object.put("sentimentName",sentimentName.toString());
                array.add(object);
            }
        }catch(Exception e){
            e.printStackTrace();
            JSONObject object = new JSONObject();
            object.put("error","Unbale to detect emotion");
            array.add(object);
        }
        return new ResponseEntity<>(array, HttpStatus.ACCEPTED);

    }
}
