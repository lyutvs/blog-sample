package com.example.springtransaction

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

const val PROPERTIES = "spring.datasource.hikari"
const val MASTER_DATASOURCE = "masterDataSource"
const val SLAVE_DATASOURCE = "slaveDataSource"

@Configuration
class DataSourceConfiguration {

    @Bean(name = [MASTER_DATASOURCE])
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    fun masterDataSource() =
        DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()

    @Bean(name = [SLAVE_DATASOURCE])
    @ConfigurationProperties("spring.datasource.slave.hikari")
    fun slaveDataSource() =
        DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
            .apply { this.isReadOnly = true }

    @Bean
    @DependsOn(MASTER_DATASOURCE, SLAVE_DATASOURCE)
    fun routingDataSource(
        @Qualifier(MASTER_DATASOURCE) masterDataSource: DataSource,
        @Qualifier(SLAVE_DATASOURCE) slaveDataSource: DataSource
    ): DataSource {
        val routingDataSource = RoutingDataSource()
        val dataSources = hashMapOf<Any, Any>()
        dataSources["master"] = masterDataSource
        dataSources["slave"] = slaveDataSource
        routingDataSource.setTargetDataSources(dataSources)
        routingDataSource.setDefaultTargetDataSource(masterDataSource)
        return routingDataSource
    }

    @Primary
    @Bean
    @DependsOn("routingDataSource")
    fun dataSource(routingDataSource: DataSource) =
        LazyConnectionDataSourceProxy(routingDataSource)
}

class RoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any =
        when {
            TransactionSynchronizationManager.isCurrentTransactionReadOnly() -> "slave"
            else -> "master"
        }
}