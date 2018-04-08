package fr.xebia.xke.sangria.jwt

import java.util.Optional

import akka.http.javadsl.model.HttpHeader
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.{HttpChallenge, ModeledCustomHeader, ModeledCustomHeaderCompanion}
import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.server.directives.RespondWithDirectives.respondWithHeaders
import akka.http.scaladsl.server.directives.{BasicDirectives, RouteDirectives}
import akka.http.scaladsl.server.{AuthenticationFailedRejection, Directive0, Directive1}
import fr.xebia.xke.sangria.models.user.User
import io.circe.parser.parse
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtOptions}

import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

trait JwtSupport {

  implicit class OptionalOps[T](optional: Optional[T]) {
    def toOption: Option[T] = if (optional.isPresent) Some(optional.get()) else None
  }

  val Algorithm = JwtAlgorithm.HS512
  val Token = "Bearer (.*)".r
  val Secret = "ABCD"

  def encode[T](t: T)(implicit e: Encoder[T]): String = {
    val userClaims = t.asJson.noSpaces
    Jwt.encode(JwtClaim(userClaims).expiresIn(3600.seconds.toSeconds), Secret, Algorithm)
  }

  def decode[T](token: String, checkExpiration: Boolean = true)(implicit d: Decoder[T]): Option[T] = {
    Jwt.decode(token, Secret, Seq(Algorithm), JwtOptions(expiration = checkExpiration)) match {
      case Success(user) => parse(user).flatMap(_.as[T]).toOption
      case Failure(_) => None
    }
  }

  def getToken(header: HttpHeader): Option[String] = {
    header.value() match {
      case Token(token) => Some(token)
      case _ => None
    }
  }

  def getToken(request: HttpRequest): Option[String] = {
    request.getHeader(JwtTokenHeader.name).toOption.flatMap(getToken)
  }

  final class JwtTokenHeader(token: String) extends ModeledCustomHeader[JwtTokenHeader] {
    override def companion = JwtTokenHeader

    override def value = s"Bearer $token"

    override def renderInResponses = true

    override def renderInRequests = true
  }

  object JwtTokenHeader extends ModeledCustomHeaderCompanion[JwtTokenHeader] {
    override def name = "Authorization"

    override def parse(value: String) = Try(new JwtTokenHeader(value))
  }

}

object JwtSupport extends JwtSupport {

  object Directives {

    import BasicDirectives._
    import RouteDirectives._

    def authenticate: Directive1[User] = {

      extract(ctx => getToken(ctx.request)).flatMap {
        case Some(token) =>
          decode[User](token) match {
            case Some(user) => provide(user)
            case None => reject(AuthenticationFailedRejection(CredentialsRejected, HttpChallenge("Bearer", "Xebia FR")))
          }

        case None => reject(AuthenticationFailedRejection(CredentialsMissing, HttpChallenge("Bearer", "Xebia FR")))
      }
    }

    def generateToken(user: User): Directive0 =
      respondWithHeaders(JwtTokenHeader(encode(user)))
  }

}