package utils

import akka.actor.ActorSystem
import akka.pattern.after

import scala.concurrent.{ExecutionContext, Future, TimeoutException}
import scala.concurrent.duration.FiniteDuration

object FutureUtils {
    private implicit val system = ActorSystem("FutureUtils")

    implicit class FutureExtensions[T](future: Future[T]) {
        def withTimeout(timeout: => Throwable, duration: FiniteDuration)(implicit ctx: ExecutionContext): Future[T] = {
            Future.firstCompletedOf(
                Seq(
                    future,
                    after(duration, system.scheduler)(Future.failed(timeout))
                )
            )
        }

        def withTimeout(message: String, duration: FiniteDuration)(implicit ctx: ExecutionContext): Future[T] = {
            withTimeout(new TimeoutException(message), duration)
        }
    }
}
