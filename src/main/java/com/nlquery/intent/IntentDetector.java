package com.nlquery.intent;

import com.domain.model.query.QueryContext;

public interface IntentDetector {
    QueryContext detectIntent(QueryContext context);
}