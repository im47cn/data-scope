package com.nlquery.entity.impl;

import com.nlquery.entity.EntityExtractionContext;
import com.nlquery.entity.EntityExtractor;
import com.nlquery.entity.EntityTag;
import com.nlquery.preprocess.PreprocessedText;

import java.util.Collections;
import java.util.List;

public class DummyEntityExtractor implements EntityExtractor {
    @Override
    public List<EntityTag> extract(PreprocessedText preprocessedText, EntityExtractionContext context) {
        return Collections.emptyList(); // Return an empty list for now
    }
}