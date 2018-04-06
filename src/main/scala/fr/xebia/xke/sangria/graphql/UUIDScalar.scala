package fr.xebia.xke.sangria.graphql

import java.util.UUID

import sangria.ast
import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object UUIDScalar {
  case object UuidViolation extends ValueCoercionViolation("Invalid UUID")

  private def parseUUID(s: String) = Try(UUID.fromString(s)) match {
    case Success(uuid) ⇒ Right(uuid)
    case Failure(_) ⇒ Left(UuidViolation)
  }

  implicit val UuidType: ScalarType[UUID] = ScalarType[UUID]("UUID",
    coerceOutput = (uuid, caps) => uuid.toString,
    coerceUserInput = {
      case s: String => parseUUID(s);
      case _ => Left(UuidViolation)
    },
    coerceInput = {
      case ast.StringValue(s, _, _, _, _) => parseUUID(s)
      case _ => Left(UuidViolation)
    }
  )
}
