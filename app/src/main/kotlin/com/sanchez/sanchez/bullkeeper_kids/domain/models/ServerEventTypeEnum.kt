package com.sanchez.sanchez.bullkeeper_kids.domain.models

/**
 * Server Event Type Enum
 */
enum class ServerEventTypeEnum {
    DELETE_ALL_SCHEDULED_BLOCK_EVENT,
    DELETE_SCHEDULED_BLOCK_EVENT,
    SCHEDULED_BLOCK_IMAGE_CHANGED_EVENT,
    SCHEDULED_BLOCK_SAVED_EVENT,
    SCHEDULED_BLOCK_STATUS_CHANGED_EVENT,
    APP_RULES_LIST_SAVED_EVENT,
    APP_RULES_SAVED_EVENT,
    ADD_PHONE_NUMBER_BLOCKED_EVENT,
    DELETE_ALL_PHONE_NUMBER_BLOCKED_EVENT,
    DELETE_PHONE_NUMBER_BLOCKED_EVENT,
    CHANGE_BED_TIME_STATUS_EVENT,
    CHANGE_LOCK_SCREEN_STATUS_EVENT,
    UNLINK_TERMINAL_EVENT
}