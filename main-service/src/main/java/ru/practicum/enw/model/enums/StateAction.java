package ru.practicum.enw.model.enums;

/**
 * Actions to event
 * SET_TO_REVIEW - send event on a moderation (for authors only)
 * CANCEL_REVIEW - cancel event (for authors only)
 * PUBLISH_EVENT - publish event (for admin only)
 * REJECT_EVENT - reject income event (for admin only)
 */
public enum StateAction {
    SENT_TO_REVIEW,
    CANCEL_REVIEW,
    PUBLISH_EVENT,
    REJECT_EVENT
}
