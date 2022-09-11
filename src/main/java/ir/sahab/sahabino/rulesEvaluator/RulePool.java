package ir.sahab.sahabino.rulesEvaluator;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RulePool {
    static RulePool singletonObject = null;
    private final ArrayList<Rule> rules = new ArrayList<>();
    private void addRules(JsonArray rulesJson) {
        for(JsonElement ruleElement:rulesJson)
            addRule(ruleElement);
    }

    private void addRule(JsonElement ruleElement) {
        Gson gson = new Gson();
        String type = ruleElement.getAsJsonObject().get("type").getAsString();
        String rule = ruleElement.getAsJsonObject().get("data").toString();
        Rule newRule = (Rule) gson.fromJson(rule, findRuleClass(type));
        rules.add(newRule);
    }
    private static Class findRuleClass(String type) {
        for(Class ruleType:Config.RULES){
            if(type.equals(ruleType.getName()))
                return ruleType;
        }
        throw new RuntimeException("Rule Class Not found");
    }
    private RulePool(){
        JsonParser jsonParser = new JsonParser();
        try {
            JsonElement rulesJsonElement = jsonParser.parse(new FileReader(Config.RULE_FILE_ADDRESS));
            JsonArray rulesJsonArray = rulesJsonElement.getAsJsonArray();
            addRules(rulesJsonArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static public RulePool getInstance(){
        if(singletonObject == null)
            singletonObject = new RulePool();
        return singletonObject;
    }
    public ArrayList<Rule> getRules(){
        return rules;
    }
}