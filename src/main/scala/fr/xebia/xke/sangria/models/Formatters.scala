package fr.xebia.xke.sangria.models

import java.time.OffsetDateTime
import java.util.UUID

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

object Formatters {
  implicit val uuidEncoder = new Encoder[UUID] {
    override def apply(uuid: UUID): Json = Json.fromString(uuid.toString)
  }

  implicit val uuidDecoder = new Decoder[UUID] {
    override def apply(c: HCursor): Result[UUID] = c.as[String].map(UUID.fromString)
  }

  implicit val offsetDateTimeEncoder = new Encoder[OffsetDateTime] {
    override def apply(date: OffsetDateTime): Json = Json.fromString(date.toString)
  }

  implicit val offsetDateTimeDecoder = new Decoder[OffsetDateTime] {
    override def apply(c: HCursor): Result[OffsetDateTime] = c.as[String].map(OffsetDateTime.parse)
  }
}
