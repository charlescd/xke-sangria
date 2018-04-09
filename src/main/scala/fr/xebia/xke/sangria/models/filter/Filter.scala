package fr.xebia.xke.sangria.models.filter

import java.time.OffsetDateTime

import akka.http.scaladsl.unmarshalling.Unmarshaller
import fr.xebia.xke.sangria.models.genre.Genre
import io.circe.generic.semiauto._
import io.circe.parser.parse
import fr.xebia.xke.sangria.models.Formatters._

case class Filter(from: Option[OffsetDateTime], to: Option[OffsetDateTime], author: Option[String], genre: Option[Genre])

object Filter {
  // REST
  implicit val decoder = deriveDecoder[Filter]
  implicit val encoder = deriveEncoder[Filter]

  // Akka http unmarshaller for query params
  implicit val filterUnmarshaller = Unmarshaller.strict(parse).map(_.flatMap(_.as[Filter])).map(_.getOrElse(throw new IllegalArgumentException("Not a valid filter")))
}
