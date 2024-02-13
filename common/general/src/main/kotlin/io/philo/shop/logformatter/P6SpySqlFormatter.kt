package io.philo.shop.logformatter

import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.P6SpyOptions
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import jakarta.annotation.PostConstruct
import org.hibernate.engine.jdbc.internal.FormatStyle
import org.springframework.context.annotation.Configuration
import org.springframework.util.StringUtils.hasText

@Configuration
class P6SpySqlFormatter : MessageFormattingStrategy {
    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = this.javaClass.getName()
    }

    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String,
        url: String,
    ): String {
        val formattedSql = formatSql(category, sql)
        return formatLog(elapsed, category, formattedSql)
    }

    private fun formatSql(category: String, sql: String): String {
        if (hasText(sql) && isStatement(category)) {
            val trimmedSQL = trim(sql)
            return if (isDdl(trimmedSQL)) {
                FormatStyle.DDL.formatter.format(sql)
            } else FormatStyle.BASIC.formatter.format(sql) // maybe this line is DML
        }
        return sql
    }

    private fun formatLog(elapsed: Long, category: String, formattedSql: String): String {
        return String.format("[%s] | %d ms | %s", category, elapsed, formatSql(category, formattedSql))
    }

    companion object {
        private fun isDdl(trimmedSQL: String): Boolean {
            return trimmedSQL.startsWith("create") || trimmedSQL.startsWith("alter") || trimmedSQL.startsWith("comment")
        }

        private fun trim(sql: String): String {
            return sql.trim { it <= ' ' }.lowercase()
        }

        private fun isStatement(category: String): Boolean {
            return Category.STATEMENT.name == category
        }
    }
}

