package com.hika.common.util

enum class OrderStatus {
    WAIT_FOR_ADMIN_CONFIRMATION,
    WAIT_FOR_USER_PAYMENT,
    WAIT_FOR_ADMIN_PAYMENT_APPROVAL,
    SHIPPING,
    REJECTED_BY_ADMIN,
    REJECTED_BY_USER,
}