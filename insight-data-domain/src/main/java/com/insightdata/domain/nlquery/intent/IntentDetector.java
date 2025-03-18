package com.insightdata.domain.nlquery.intent;

import com.insightdata.domain.nlquery.QueryContext;

public interface IntentDetector {
    QueryContext detectIntent(QueryContext context);
}