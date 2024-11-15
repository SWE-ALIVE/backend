package main.kotlin.com.example.demo.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(name = "device_usage_records")
data class DeviceUsageRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_device_id", nullable = false)
    val userDevice: UserDevice,

    @Column(nullable = false)
    val usageDate: LocalDate,

    @Column(nullable = false)
    val actionDescription: String,

    @Column
    val startTime: LocalDateTime,

    @Column
    val endTime: LocalDateTime
)