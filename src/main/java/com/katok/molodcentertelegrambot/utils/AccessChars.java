package com.katok.molodcentertelegrambot.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AccessChars {
    public final static Set<Character> accessChars = Collections.unmodifiableSet(new HashSet<>() {{
        for (char letter : "АаБбВвГгҐґДдЕеЄєЖжЗзИиІіЇїЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЬьЮюЯя' ".toCharArray()) {
            add(letter);
        }
    }});
}
