package com.alerts;

/**
 * Base decorator that wraps an alert while keeping the same interface.
 */
public abstract class AlertDecorator implements Alert {

    protected final Alert alert;

    /**
     * Wraps an existing alert.
     *
     * @param alert the alert to decorate
     */
    public AlertDecorator(Alert alert) {
        this.alert = alert;
    }

    @Override
    public String getPatientId() {
        return alert.getPatientId();
    }

    @Override
    public String getCondition() {
        return alert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return alert.getTimestamp();
    }
}
