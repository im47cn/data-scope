package com.insightdata.domain.nlquery.entity.impl;

import com.insightdata.domain.nlquery.entity.EntityExtractionContext;
import com.insightdata.domain.nlquery.entity.EntityExtractor;
import com.insightdata.domain.nlquery.entity.EntityTag;
import com.insightdata.domain.nlquery.preprocess.PreprocessedText;

import java.util.Collections;
import java.util.List;

public class DummyEntityExtractor implements EntityExtractor {
    @Override
    public List<EntityTag> extract(PreprocessedText preprocessedText, EntityExtractionContext context) {
        return Collections.emptyList(); // Return an empty list for now
    }
}