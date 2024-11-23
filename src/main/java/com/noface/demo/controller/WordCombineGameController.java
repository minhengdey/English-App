package com.noface.demo.controller;

import com.noface.demo.screen.WordCombineGameScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class WordCombineGameController {
    private WordCombineGameScreen screen;
    private ListProperty<String> words = new SimpleListProperty(FXCollections.observableArrayList());
    public WordCombineGameController() throws IOException {
        getWordsData();
        screen = new WordCombineGameScreen(this);
    }
    public void getWordsData(){
        words.clear();
        words.addAll(List.of(
                "abacus", "abandon", "abandoned", "abandonment", "abate", "abbreviation", "abdomen", "abduct", "abduction",
                "abundant", "abuse", "academic", "academy", "accelerate", "accept", "acceptable", "acceptance", "access", "accessible",
                "accident", "accommodation", "accompany", "accomplish", "accord", "account", "accuse", "achieve", "achievement", "acquire",
                "acquisition", "acquit", "acre", "act", "action", "active", "actor", "actress", "actual", "adapt", "addition", "address",
                "admit", "adopt", "advance", "adventure", "advice", "advise", "affect", "affection", "afford", "affordable", "against",
                "agency", "agenda", "aggregate", "aggressive", "agriculture", "alcohol", "alert", "alien", "alike", "alive", "alliance",
                "allocate", "allow", "allowance", "almost", "aloud", "already", "also", "alter", "alternative", "although", "altogether",
                "amazing", "ambition", "amend", "amendment", "amount", "amplify", "analysis", "analyze", "ancient", "anger", "angle", "animal",
                "annual", "answer", "anxiety", "anxious", "apology", "appeal", "appeal", "appear", "appearance", "appetite", "appliance", "apply",
                "appointment", "approve", "aquatic", "area", "arrange", "arrangement", "arrest", "arrival", "article", "artificial", "artist",
                "assert", "assess", "assessment", "asset", "assist", "assistance", "associate", "association", "assume", "assumption", "athlete",
                "attempt", "attention", "attitude", "attract", "attraction", "auction", "audible", "audience", "author", "authority", "automate",
                "automatic", "available", "average", "aversion", "backbone", "bachelor", "balance", "banish", "barrier", "base", "basic",
                "basics", "battery", "beacon", "believe", "benefit", "bicycle", "biology", "blessing", "boast", "bother", "boundary", "bravery",
                "brother", "brutal", "budget", "burden", "cabinet", "capital", "captain", "capture", "careful", "career", "caring", "cattle",
                "celebrate", "censorship", "ceremony", "chaos", "channel", "charity", "charter", "cheap", "check", "chronic", "circuit", "citizen",
                "climate", "clinic", "clothing", "clumsy", "coarse", "collapse", "college", "column", "combine", "comfort", "comment", "commitment",
                "common", "complete", "complex", "comply", "conclude", "concrete", "conference", "contribute", "courage", "create", "current",
                "cushion", "cycle", "daily", "damage", "danger", "daughter", "debate", "decade", "defend", "defender", "delicate", "demand",
                "deprive", "detect", "develop", "device", "dialect", "diet", "difficult", "digital", "dilute", "direct", "disease", "distant",
                "document", "doubt", "dynamic", "dysfunction", "eager", "eagle", "early", "economy", "eclipse", "editor", "education", "effort",
                "elevate", "endure", "enough", "ensure", "escape", "essence", "estate", "estimate", "ethical", "evidence", "example", "examine", "example", "exceed", "exclude", "exclusive", "excuse", "expand", "expect", "experience", "explain", "explore",
                "export", "expose", "express", "extend", "external", "extinct", "extract", "extreme", "eye", "fabric", "famous", "fashion",
                "feature", "federal", "feedback", "festival", "fiction", "figure", "final", "finance", "finish", "fishing", "fitness",
                "flavor", "flexible", "floating", "focus", "follow", "force", "forever", "formal", "formula", "fortune", "freedom", "friend",
                "friendly", "front", "future", "gallery", "gather", "genuine", "giant", "global", "govern", "gossip", "grace", "gradual",
                "graduate", "gravity", "guitar", "happen", "harbor", "harmony", "hazard", "healthy", "hearing", "heart", "height", "heritage",
                "hero", "hesitate", "hockey", "honest", "horizon", "hospital", "host", "hotel", "house", "housing", "human", "humor", "hunger",
                "identity", "ignore", "image", "impact", "import", "impose", "improve", "include", "income", "increase", "independent",
                "indicate", "industry", "influence", "inform", "inspire", "install", "instant", "intact", "interest", "internet", "interval",
                "invest", "invite", "involve", "issue", "jacket", "jazz", "jealous", "joke", "journal", "journey", "judge", "jungle", "junior",
                "justice", "keen", "keyboard", "knowledge", "labor", "language", "laptop", "latter", "laugh", "leader", "learn", "lecture",
                "legend", "library", "lifetime", "limited", "literacy", "literature", "logical", "lounge", "lucky", "luxury", "maintain",
                "manager", "manual", "mansion", "market", "marriage", "massive", "meaning", "measure", "member", "memory", "mention", "merit",
                "message", "method", "mobile", "moment", "morning", "motion", "natural", "navigate", "neither", "nervous", "network", "noble",
                "normal", "notable", "notice", "number", "object", "observe", "office", "online", "option", "origin", "outcome", "outside",
                "overcome", "package", "passion", "patient", "pattern", "peaceful", "penalty", "perform", "permit", "person", "plastic",
                "planet", "player", "police", "popular", "prepare", "privacy", "produce", "project", "promote", "protect", "provide", "publish",
                "purpose", "quality", "quickly", "question", "quota", "reaction", "reality", "receive", "reform", "reject", "release", "remote",
                "replace", "reputation", "request", "rescue", "resign", "resource", "response", "restore", "revenue", "revert", "review", "revolt",
                "reward", "rising", "sample", "season", "second", "select", "serious", "service", "session", "settle", "severe", "shining",
                "sight", "simple", "situation", "society", "source", "special", "speech", "sponsor", "stable", "standard", "station", "student",
                "subject", "success", "suggest", "supply", "surface", "symbol", "system", "target", "team", "television", "tension", "theory",
                "thick", "through", "ticket", "timber", "together", "traffic", "training", "treat", "trust", "unique", "united", "universal",
                "unusual", "vacant", "valley", "victory", "village", "visible", "vocation", "volume", "waiting", "weather", "western", "whisper",
                "window", "witness", "wonder", "workplace", "worthy", "wrist", "yesterday", "youngster", "youthful", "zenith", "zinc"));
    }

    public WordCombineGameScreen getScreen() {
        return screen;
    }

    public ObservableList<String> getWords() {
        return words.get();
    }

    public ListProperty<String> wordsProperty() {
        return words;
    }
}
