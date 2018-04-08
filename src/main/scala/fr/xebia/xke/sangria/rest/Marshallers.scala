package fr.xebia.xke.sangria.rest

import java.time.OffsetDateTime

import akka.http.scaladsl.unmarshalling.Unmarshaller

object Marshallers {
  implicit val dateUnmarshaller: Unmarshaller[String, OffsetDateTime] = Unmarshaller.strict(OffsetDateTime.parse)
}
