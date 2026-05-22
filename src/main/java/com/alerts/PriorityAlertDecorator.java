package com.alerts;

/**
 * Adds a priority level to an alert.
 */
public class PriorityAlertDecorator extends AlertDecorator {

    private final String priority;

    /**
     * Creates a priority alert decorator.
     *
     * @param alert    the alert to decorate
     * @param priority the priority level to attach
     */
    public PriorityAlertDecorator(Alert alert, String priority) {
        super(alert);
        this.priority = priority;
    }

    @Override
    public String getCondition() {
        return "[" + priority + "] " + alert.getCondition();
    }

    /**
     * Gets the priority level.
     *
     * @return the priority level
     */
    public String getPriority() {
        return priority;
    }
}
