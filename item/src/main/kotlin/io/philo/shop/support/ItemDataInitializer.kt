package io.philo.shop.support

import io.philo.shop.domain.entity.ItemEntity
import io.philo.shop.repository.ItemRepository
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.SQLException
import java.sql.SQLTimeoutException
import javax.sql.DataSource

@Component
@Slf4j
class ItemDataInitializer(
    private val dataSource: DataSource,
    private val itemRepository: ItemRepository
) : ApplicationListener<ApplicationStartedEvent> {

    private val log = KotlinLogging.logger { }

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        checkConnection()
        initItems()
    }

    private fun checkConnection() {
        val connection: Connection? = getConnection()
        if (connection === null) {
            log.error { "[DB Connection Fail] ${this.javaClass.simpleName}" }
        }
        log.info("[DB Connection Success] ${this.javaClass.simpleName}")
    }

    private fun getConnection(): Connection? {
        return try {
            dataSource.connection
        } catch (e: SQLException) {
            log.error { "[DB Connection Fail] ${this.javaClass.simpleName}" }
            throw RuntimeException(e)
        } catch (e: SQLTimeoutException) {
            log.error { "[DB Connection Fail] ${this.javaClass.simpleName}" }
            throw RuntimeException(e)
        }
    }

    private fun initItems() {

        try {

            val item1 = ItemEntity(name = "초신사 스탠다드 블랙 스웨트 셔츠 오버 핏", size = "90-S", price = 49_800, stockQuantity = 1_000)
            val item2 = ItemEntity(name = "초신사 스탠다드 블랙 스웨트 셔츠 오버 핏", size = "100-M", price = 49_800, stockQuantity = 1_000)
            val item3 = ItemEntity(name = "초신사 스탠다드 블랙 스웨트 셔츠 오버 핏", size = "110-L", price = 49_800, stockQuantity = 1_000)

            val item4 = ItemEntity(name = "드로우핏 네이비 발마칸 코트 세미 오버 핏", price = 245_000, stockQuantity = 200)

            itemRepository.saveAll(listOf(item1, item2, item3, item4))
        } catch (e: Exception) {
            // 데이터 초기화 에러
            log.error { "[Data Initialization error]" }
            e.printStackTrace()
            val all = itemRepository.findAll()
            log.info { "item list = $all" }
        }
    }
}
