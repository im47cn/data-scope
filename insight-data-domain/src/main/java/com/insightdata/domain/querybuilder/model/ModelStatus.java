package com.insightdata.domain.querybuilder.model;

/**
 * Enumeration of possible statuses for a query model.
 */
public enum ModelStatus {
    DRAFT,      // The model is being created or edited
    ACTIVE,     // The model is ready for use
    INACTIVE,   // The model is not currently in use
    DEPRECATED, // The model should no longer be used
    DELETED     // The model has been deleted
}