package streamprocessing.admin

import org.apache.kafka.clients.admin._

import java.util

trait KafkaAdminClient {
  def createTopics(newTopics: util.Collection[NewTopic]): CreateTopicsResult

  def listTopics(): ListTopicsResult

  def describeConsumerGroups(groupIds: Seq[String], options: DescribeConsumerGroupsOptions): DescribeConsumerGroupsResult

  def listConsumerGroupOffsets(groupId: String, options: ListConsumerGroupOffsetsOptions): ListConsumerGroupOffsetsResult

  def topicExists(topicName: String): Boolean
}
