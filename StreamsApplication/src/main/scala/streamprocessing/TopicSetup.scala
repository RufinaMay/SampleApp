package streamprocessing

import com.typesafe.config.Config
import scaldi.{Injectable, Injector}
import streamprocessing.admin.KafkaAdminClient

import scala.annotation.tailrec

class TopicSetup(implicit inj: Injector) extends Injectable {
  private lazy val kafkaAdmin: KafkaAdminClient = inject[KafkaAdminClient]
  private val config: Config = inject[Config]

  private val retryTimeAvailableTopicsMs = config.getLong("kafka.topic.retryTimeAvailableTopicsMs")
  private val maxWaitingTimeMs = config.getLong("kafka.topic.maxWaitingTimeMs")

  def isInputTopicReady(topicName: String): Boolean =
    topicExists(topicName)

  private def topicExists(topicName: String, shouldRetry: Boolean = false): Boolean = {
    @tailrec
    def retryTopicReady(msLeft: Long): Boolean = {
      if (kafkaAdmin.topicExists(topicName)) {
        true
      } else {
        if (msLeft <= 0) {
          false
        } else {
          Thread.sleep(retryTimeAvailableTopicsMs)
          retryTopicReady(msLeft - retryTimeAvailableTopicsMs)
        }
      }
    }

    if (shouldRetry) retryTopicReady(maxWaitingTimeMs) else retryTopicReady(0)
  }
}
