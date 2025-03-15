package com.insightdata.nlquery.preprocess.tokenizer;

import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class HanlpTokenizer implements Tokenizer {

    private static final List<Nature> SKIP_NATURES = new ArrayList<Nature>() {{
        add(Nature.uj); // 助词，比如“的”、“了”、“着”
    }};

    static {
        CustomDictionary.add("升序");
        CustomDictionary.add("降序");
    }

    @Override
    public List<String> tokenize(String text) {
        return segment(text);
    }

    @Override
    public List<String> tokenize(String text, String language) {
        return segment(text);
    }

    /**
     * 分词
     *
     * @param content 内容
     */
    private static List<String> segment(String content) {
        List<Term> terms = HanLP.segment(content);
        List<String> companySet = new ArrayList<>();

        for (Term term : terms) {
            String name = term.word;

            // 过滤掉标点符号
            if (SKIP_NATURES.contains(term.nature)) {
               continue;
            }

            companySet.add(name);
        }
        log.info("segment 分词结果:{}, 提取结果:{}", terms, companySet);
        return companySet;
    }

}
