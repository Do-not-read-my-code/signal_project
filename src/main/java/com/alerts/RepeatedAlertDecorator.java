package com.alerts;

/**
 * Marks an alert as repeated and tracks how many times it occurred.
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    private final int repeatCount;

    /**
     * Creates a repeated alert decorator.
     *
     * @param alert       the alert to decorate
     * @param repeatCount how many times the condition appeared
     */
    public RepeatedAlertDecorator(Alert alert, int repeatCount) {
        super(alert);
        this.repeatCount = repeatCount;
    }

    @Override
    public String getCondition() {
        return alert.getCondition() + " repeated " + repeatCount + " times";
    }

    /**
     * Gets how many times the alert repeated.
     *
     * @return the repeat count
     */
    public int getRepeatCount() {
        return repeatCount;
    }
}
