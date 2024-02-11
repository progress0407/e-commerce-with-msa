package io.philo.shop.error

class EntityNotFoundException(
    entityInfo: Any,
    cause: Throwable?
) :
    NotFoundException("엔티티를 찾을 수 없습니다: $entityInfo", cause) {

    constructor(condition: Any) : this(condition, null)
}