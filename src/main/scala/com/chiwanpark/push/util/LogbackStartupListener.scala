package com.chiwanpark.push.util

import ch.qos.logback.classic.spi.LoggerContextListener
import ch.qos.logback.classic.{Level, Logger, LoggerContext}
import ch.qos.logback.core.spi.{ContextAwareBase, LifeCycle}
import com.chiwanpark.push.Configuration

class LogbackStartupListener extends ContextAwareBase with LoggerContextListener with LifeCycle {
  var started = false

  override def isStarted: Boolean = started
  override def isResetResistant: Boolean = true

  override def start(): Unit = {
    if (started) return
    val context = getContext
    val logLevel = if (Configuration.DEBUG) "debug" else "info"
    context.putProperty("LOG_LEVEL", logLevel)

    started = true
  }

  // Not used
  override def onReset(context: LoggerContext): Unit = {}
  override def onStart(context: LoggerContext): Unit = {}
  override def onLevelChange(logger: Logger, level: Level): Unit = {}
  override def onStop(context: LoggerContext): Unit = {}
  override def stop(): Unit = {}
}
