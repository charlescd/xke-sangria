package fr.xebia.xke.sangria.graphql

import java.time.OffsetDateTime
import java.util.UUID

import sangria.ast
import sangria.marshalling.DateSupport
import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object Scalar {
  case object UuidViolation extends ValueCoercionViolation("Invalid UUID")
  case object DateViolation extends ValueCoercionViolation("Invalid Date")

  private def parseUUID(s: String) = Try(UUID.fromString(s)) match {
    case Success(uuid) ⇒ Right(uuid)
    case Failure(_) ⇒ Left(UuidViolation)
  }

  implicit val UuidType: ScalarType[UUID] = ScalarType[UUID]("UUID",
    coerceOutput = (uuid, _) => uuid.toString,
    coerceUserInput = {
      case s: String => parseUUID(s);
      case _ => Left(UuidViolation)
    },
    coerceInput = {
      case ast.StringValue(s, _, _, _, _) => parseUUID(s)
      case _ => Left(UuidViolation)
    }
  )

  private def parseDate(s: String) = Try(OffsetDateTime.parse(s)) match {
    case Success(date) ⇒ Right(date)
    case Failure(_) ⇒ Left(DateViolation)
  }

  implicit val OffsetDateTimeType: ScalarType[OffsetDateTime] = ScalarType[OffsetDateTime]("OffsetDateTime",
    coerceOutput = (date, caps) =>
      if (caps.contains(DateSupport)) date.toLocalDate
      else date.toString,
    coerceUserInput = {
      case s: String => parseDate(s)
      case _ => Left(DateViolation)
    },
    coerceInput = {
      case ast.StringValue(s, _, _, _, _) ⇒ parseDate(s)
      case _ ⇒ Left(DateViolation)
    }
  )

}
